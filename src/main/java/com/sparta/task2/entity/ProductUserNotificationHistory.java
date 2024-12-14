package com.sparta.task2.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "ProductUserNotificationHistory", indexes = {
        @Index(name = "idx_product_user_round", columnList = "productId, userId, restockRound"),
        @Index(name = "idx_sent_date", columnList = "sentDate")
})
public class ProductUserNotificationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product productId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private ProductUserNotification userNotification;

    private int restockRound;

    private LocalDateTime sentDate;


}
