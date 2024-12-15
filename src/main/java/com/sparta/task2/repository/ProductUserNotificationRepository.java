package com.sparta.task2.repository;

import com.sparta.task2.entity.ProductUserNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductUserNotificationRepository extends JpaRepository<ProductUserNotification, Long> {
    List<ProductUserNotification> findByProductProductIdAndIsActive(Long productId, boolean isActive);

    List<ProductUserNotification> findByProductProductId(Long productId);
}
