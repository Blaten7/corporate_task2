package com.sparta.task2.repository;

import com.sparta.task2.entity.Product;
import com.sparta.task2.entity.ProductUserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductUserNotificationRepository extends JpaRepository<ProductUserNotification, Long> {
    // 알림 신청 유저 조회 메서드 (createdAt 기준 정렬)
    List<ProductUserNotification> findByProductProductIdAndIsActiveOrderByCreatedAtAsc(Long productId, boolean isActive);

    List<ProductUserNotification> findByProductProductId(Long productId);

    @Query("SELECT pun FROM ProductUserNotification pun " +
            "WHERE pun.product.productId = :productId " +
            "AND pun.isActive = true " +
            "AND pun.userId NOT IN (" +
            "   SELECT pnh.lastSentUserId " +
            "   FROM ProductNotificationHistory pnh " +
            "   WHERE pnh.product.productId = :productId " +
            "   AND pnh.notificationStatus = 'COMPLETED'" +
            ") " +
            "ORDER BY pun.createdAt ASC")
    List<ProductUserNotification> findActiveNotificationsExcludingCompleted(@Param("productId") Long productId);

    boolean existsByProductAndUserId(Product product, long i);
}
