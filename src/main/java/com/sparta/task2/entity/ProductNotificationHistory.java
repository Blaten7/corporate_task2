package com.sparta.task2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "ProductNotificationHistory", indexes = {
        @Index(name = "idx_product_round", columnList = "productId, restockRound"),
        @Index(name = "idx_notification_status", columnList = "notificationStatus"),
        @Index(name = "idx_last_sent_user", columnList = "lastSentUserId")
})
// 상품별 재입고 알림 히스토리
public class ProductNotificationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK 설정

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    private int restockRound;

    private String notificationStatus;

    private Long lastSentUserId;
}