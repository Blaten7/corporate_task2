package com.sparta.task2.component;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificationSender {
    private static final Logger log = LoggerFactory.getLogger(NotificationSender.class);

    // 알림 전송 로직
    public void sendNotification(Long userId, Long productId) {
        try {
            // 예시: 이메일 또는 SMS 전송 로직
            log.info("사용자{}에게 상품{} 재입고 알림!", userId, productId);
        } catch (Exception e) {
            log.info("Failed to send notification to user {} for product {}", userId, productId);
        }
    }
}
