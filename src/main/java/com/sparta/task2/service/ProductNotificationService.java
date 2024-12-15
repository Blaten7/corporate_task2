package com.sparta.task2.service;

import com.sparta.task2.entity.Product;
import com.sparta.task2.entity.ProductNotificationHistory;
import com.sparta.task2.entity.ProductUserNotification;
import com.sparta.task2.entity.ProductUserNotificationHistory;
import com.sparta.task2.repository.ProductNotificationHistoryRepository;
import com.sparta.task2.repository.ProductRepository;
import com.sparta.task2.repository.ProductUserNotificationRepository;
import jakarta.transaction.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductNotificationService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductUserNotificationRepository notificationRepository;

    @Autowired
    private ProductNotificationHistoryRepository historyRepository;

    @Transactional
    public void sendRestockNotifications(Long productId) {
        // 상품 정보 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

        // 재입고 회차 증가
        product.setRestockRound(product.getRestockRound() + 1);
        productRepository.save(product);

        // 알림 기록 생성
        ProductNotificationHistory notificationHistory = new ProductNotificationHistory();
        notificationHistory.setProduct(product);
        notificationHistory.setRestockRound(product.getRestockRound());
        notificationHistory.setNotificationStatus("IN_PROGRESS");
        historyRepository.save(notificationHistory);

        // 알림 대상 유저 조회
        List<ProductUserNotification> notifications = notificationRepository.findByProductProductId(productId);

        // 알림 전송
        for (ProductUserNotification userNotification : notifications) {
            if (product.getStockStatus() <= 0) {
                // 재고 부족 시 중단
                notificationHistory.setNotificationStatus("CANCELED_BY_SOLD_OUT");
                historyRepository.save(notificationHistory);
                break;
            }

            try {
                // 알림 전송 로직
                sendNotification(userNotification);

                // 알림 히스토리 기록

                ProductUserNotificationHistory userNotificationHistory = new ProductUserNotificationHistory();
                userNotificationHistory.setProductId(product);
                userNotificationHistory.setUserNotification(userNotification);
                userNotificationHistory.setRestockRound(product.getRestockRound());
                userNotificationHistory.setSentDate(LocalDateTime.now());
                notificationRepository.save(userNotification);

                // 재고 감소 처리 (가정: 알림 1건당 재고 감소 1)
                product.setStockStatus(product.getStockStatus() - 1);
                productRepository.save(product);

            } catch (Exception e) {
                // 알림 중 예외 발생 시 중단
                notificationHistory.setNotificationStatus("CANCELED_BY_ERROR");
                historyRepository.save(notificationHistory);
                throw e; // 예외 전파
            }
        }

        // 알림 상태 완료로 업데이트
        if ("IN_PROGRESS".equals(notificationHistory.getNotificationStatus())) {
            notificationHistory.setNotificationStatus("COMPLETED");
            historyRepository.save(notificationHistory);
        }
    }

    private void sendNotification(ProductUserNotification userNotification) {
        // 알림 메시지 전송 로직
        System.out.println("Sending notification to user: " + userNotification.getUserId());
    }
}
