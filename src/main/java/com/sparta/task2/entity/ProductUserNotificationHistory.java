package com.sparta.task2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "ProductUserNotificationHistory", indexes = {
        @Index(name = "idx_product_user_round", columnList = "productId, userId, restockRound"),
        @Index(name = "idx_sent_date", columnList = "sentDate")
})
public class ProductUserNotificationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private ProductUserNotification userNotification;

    private int restockRound;

    private LocalDateTime sentDate;
}
