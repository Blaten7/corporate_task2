package com.sparta.task2.repository;

import com.sparta.task2.entity.Product;
import com.sparta.task2.entity.ProductNotificationHistory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductNotificationHistoryRepository extends JpaRepository<ProductNotificationHistory, Long> {

    // 알림 전송시 로그 저장
    @Modifying
    @Transactional
    @Query(value = "insert into product_notification_history " +
            "(last_sent_user_id, notification_status, restock_round, product_id) " +
            "values (:userId, 'IN_PROGRESS',:restockRound, :productId)", nativeQuery = true)
    void saveNoticeLog(Long productId, int restockRound, Long userId);

    boolean existsByProduct_ProductId(long productId);

    // 알림 요청 받았을때 상품 재입고 알림 로그 테이블에 재입고 회차와 재입고 알림 발송 상태를 업데이트
    @Modifying
    @Transactional
    @Query("UPDATE ProductNotificationHistory PNH " +
            "SET PNH.notificationStatus = 'COMPLETED' " +
            "WHERE PNH.product = :product")
    void updateComplete(@Param("product") Product product);

    @Modifying
    @Transactional
    @Query("UPDATE ProductNotificationHistory PNH " +
            "SET PNH.restockRound = PNH.restockRound + 1, " +
            "    PNH.notificationStatus = 'IN_PROGRESS' " +
            "WHERE PNH.product = :product")
    void updateRestockRoundAndNotificationStatus(@Param("product") Product product);

    @Modifying
    @Transactional
    @Query("UPDATE ProductNotificationHistory PNH " +
            "SET PNH.notificationStatus = 'CANCELED_BY_SOLD_OUT' " +
            "WHERE PNH.product = :product")
    void saveNoticeLogTableCurrentStatus(@Param("product") Product product);

    @Modifying
    @Transactional
    @Query("UPDATE ProductNotificationHistory PNH " +
            "SET PNH.notificationStatus = 'CANCELED_BY_ERROR' " +
            "WHERE PNH.product = :product")
    void saveNoticeLogTableCurrentStatus2(@Param("product") Product product);

}
