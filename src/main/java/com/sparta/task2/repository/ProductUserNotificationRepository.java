package com.sparta.task2.repository;

import com.sparta.task2.entity.ProductUserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductUserNotificationRepository extends JpaRepository<ProductUserNotification, Long> {
    List<ProductUserNotification> findByProductProductIdAndActive(Long productId, boolean isActive);

    List<ProductUserNotification> findByProductProductId(Long productId);
}
