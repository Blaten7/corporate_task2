package com.sparta.task2.service;

import com.sparta.task2.dto.NotificationRequestDto;
import com.sparta.task2.entity.Product;
import com.sparta.task2.entity.ProductUserNotification;
import com.sparta.task2.repository.ProductRepository;
import com.sparta.task2.repository.ProductUserNotificationRepository;
import lombok.AllArgsConstructor;
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
    private final RedisTemplate<String, String> redisTemplate;
    private final ProductUserNotificationRepository productUserNotificationRepository;

    // 재입고 알림 요청 처리
    @Transactional
    public void processRestockNotifications(Long productId) {
        // 1. 상품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        // 2. 알림 신청 유저 조회
        List<ProductUserNotification> notifications = userNotificationRepository.findByProductProductIdAndActive(productId, true);

        if (notifications.isEmpty()) {
            throw new IllegalStateException("No active notifications for this product.");
        }

        // 3. Redis 큐에 알림 요청 저장
        for (ProductUserNotification notification : notifications) {
            NotificationRequestDto request = new NotificationRequestDto(notification.getUserId(), product.getProductId());
            redisTemplate.opsForList().rightPush("restock-notifications", String.valueOf(request));
        }

        System.out.printf("Queued %d notifications for product %d%n", notifications.size(), productId);
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
