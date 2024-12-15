package com.sparta.task2.repository;

import com.sparta.task2.entity.ProductUserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductUserNotificationRepository extends JpaRepository<ProductUserNotification, Long> {
    // 알림 신청 유저 조회 메서드 (createdAt 기준 정렬)
    List<ProductUserNotification> findByProductProductIdAndIsActiveOrderByCreatedAtAsc(Long productId, boolean isActive);

    List<ProductUserNotification> findByProductProductId(Long productId);
}
