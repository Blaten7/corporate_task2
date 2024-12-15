package com.sparta.task2.component;

import org.springframework.stereotype.Component;

@Component
public class NotificationSender {

    // 알림 전송 로직
    public void sendNotification(Long userId, Long productId) {
        try {
            // 예시: 이메일 또는 SMS 전송 로직
            System.out.printf("Sending restock notification to user %d for product %d%n", userId, productId);
        } catch (Exception e) {
            System.err.printf("Failed to send notification to user %d for product %d%n", userId, productId);
        }
    }
}
