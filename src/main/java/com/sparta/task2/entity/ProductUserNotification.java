package com.sparta.task2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "product_user_notification", indexes = {
        @Index(name = "idx_product_user", columnList = "productId, userId"),
        @Index(name = "idx_active_created", columnList = "isActive, createdAt")
})
public class ProductUserNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId; // PK 설정

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    private boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
