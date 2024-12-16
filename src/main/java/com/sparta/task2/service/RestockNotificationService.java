package com.sparta.task2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.task2.component.NotificationSender;
import com.sparta.task2.dto.NotificationRequestDto;
import com.sparta.task2.dto.RestockNotificationRequest;
import com.sparta.task2.entity.Product;
import com.sparta.task2.entity.ProductNotificationHistory;
import com.sparta.task2.entity.ProductUserNotification;
import com.sparta.task2.repository.ProductNotificationHistoryRepository;
import com.sparta.task2.repository.ProductRepository;
import com.sparta.task2.repository.ProductUserNotificationHistoryRepository;
import com.sparta.task2.repository.ProductUserNotificationRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.redis.core.RedisTemplate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class RestockNotificationService {

    private final ProductRepository productRepository;
    private final ProductUserNotificationRepository userNotificationRepository;
    private final ProductNotificationHistoryRepository notificationHistoryRepository;
    private final ProductUserNotificationHistoryRepository userNotificationHistoryRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final ProductUserNotificationRepository productUserNotificationRepository;
    private final NotificationSender notificationSender;
    private static final Logger log = LoggerFactory.getLogger(RestockNotificationService.class);

    // 재입고 알림 요청 처리
    @Transactional
    public void processRestockNotifications(RestockNotificationRequest rnr) {
        long productId = rnr.getProductId();
        long quantity = rnr.getQuantity();
        // 1. 상품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        // 2-1. 해당 상품의 재입고 회차 증가, 재고 수량 증가
        productRepository.updateRestockRoundAndStockStatusById(productId, quantity);
        // 2-2. 해당 상품의 재입고 알림 히스토리에 재입고 회차와 재입고 알림 발송상태를 저장
        boolean findId = notificationHistoryRepository.existsByProduct_ProductId(productId);
        ProductNotificationHistory pnh = new ProductNotificationHistory();
        if (!findId) {
            pnh.setProduct(product);
            pnh.setRestockRound(0);
            pnh.setLastSentUserId(null);
            pnh.setNotificationStatus("IN_PROGRESS");
//            notificationHistoryRepository.saveNoticeLogTable(products);
            notificationHistoryRepository.save(pnh);
        }
        notificationHistoryRepository.updateRestockRoundAndNotificationStatus(product);

        // 3. 알림 신청 유저 조회
        List<ProductUserNotification> notifications = userNotificationRepository.findByProductProductIdAndIsActiveOrderByCreatedAtAsc(productId, true);
        if (notifications.isEmpty()) {
            throw new IllegalStateException("No active notifications for this product.");
        }

        // 3-1. Redis 큐 리스트 초기화
        redisTemplate.opsForList().trim("restock-notifications-keys", 1, 0); // 빈 리스트 유지
        log.info("Redis 큐 '{}'의 데이터를 모두 삭제했습니다.", "restock-notifications-keys");
        // 3-2. Redis 큐에 알림 요청 저장
        for (ProductUserNotification notification : notifications) {
            log.info("\n알림 신청한 유저 정보 : {}", notification);
            try {
                NotificationRequestDto request = new NotificationRequestDto(notification.getUserId(), product.getProductId());
                // JSON 직렬화 후 Redis에 저장
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonRequest = objectMapper.writeValueAsString(request);
                redisTemplate.opsForList().rightPush("restock-notifications-keys", jsonRequest);
                log.info("\nRedis 큐에 저장 완료 : {}", jsonRequest);
            } catch (Exception e) {
                log.error("\nRedis 저장 중 오류 발생. \nNotification 정보: {}\n, 에러: {}", notification, e.getMessage(), e);
            }
        }
        System.out.printf("Queued %d notifications for product %d%n", notifications.size(), productId);

        boolean completeFlag = true;

        int currentStock = productRepository.findByIdStockStatus(productId);
        log.info("[알림 발송 전] 현재 재고량 : {}", currentStock);

        // 4. Redis 큐에 저장된 순서대로 알림을 발송
        for (int i = 0; i < notifications.size(); i++) {
            log.info("Redis 큐에 저장된 순서대로 알림을 발송" + (i + 1) + "회차 진행중\n 레디스 큐 사이즈 : {}", notifications.size());
            String jsonRequest = redisTemplate.opsForList().leftPop("restock-notifications-keys");

            if (jsonRequest == null) {
                completeFlag = false;
                log.info("큐에 저장된 값이 없음!");
                System.out.println("No more notifications to process.");
                break;
            }

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                NotificationRequestDto request = objectMapper.readValue(jsonRequest, NotificationRequestDto.class);
                long userId = request.getUserId();
                long productIds = request.getProductId();

                // 5-2. 재입고 알림을 발송한 유저 정보 저장
                log.info("자 이제 유저 정보를 저장할건데");
                int restockRound = productRepository.findByIdRestockRound(productIds);
                userNotificationHistoryRepository.saveAllTo(userId, productIds, restockRound);
                System.out.println("저장이 잘 되었다");

                // 알림 전송
                notificationSender.sendNotification(userId, productIds);
                System.out.println("알림 전송도 끝");
                // 알림 전송시 상품별 재입고 알림 히스토리에 저장
                notificationHistoryRepository.saveNoticeLog(productIds, restockRound, userId);
                log.info("알림 전송시 상품별 재입고 알림 히스토리에 저장까지 완료");
                // 5-1. 재입고 알림 발송중 품절 [ CANCELED_BY_SOLD_OUT ] 또는 예외 [ CANCELED_BY_ERROR ] 발생시 알림발송 중단,
//                productRepository.updateStockById(productId); // stockStatusException 품절 예외 강제 발생
                currentStock = productRepository.findByIdStockStatus(productId);
                if (currentStock < 1) {
                    log.warn("품절 예외 발생 // 현재 재고량 : {}", currentStock);
                    notificationHistoryRepository.saveNoticeLogTableCurrentStatus(product);
                    completeFlag = false;
                    break;
                }
            } catch (Exception e) {
                log.warn("좆같은 에러 발생");
                completeFlag = false;
                notificationHistoryRepository.saveNoticeLogTableCurrentStatus2(product);
                System.err.println("Failed to process notification: " + e.getMessage());
            }
        }

        // 6. 모든 사용자에게 알림 요청을 다 보냈다면 완료상태 COMPLETED 를 DB에 저장
        if (completeFlag) notificationHistoryRepository.updateComplete(product);
    }

    @Transactional
    public void saveUserNotification(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        ProductUserNotification notification = new ProductUserNotification();
        notification.setProduct(product);
        notification.setActive(true);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setUpdatedAt(LocalDateTime.now());

        productUserNotificationRepository.save(notification); // userId는 자동 생성
    }

}
