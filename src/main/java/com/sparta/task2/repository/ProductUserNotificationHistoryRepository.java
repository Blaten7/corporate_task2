package com.sparta.task2.repository;

import com.sparta.task2.entity.ProductUserNotificationHistory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductUserNotificationHistoryRepository extends JpaRepository<ProductUserNotificationHistory, Long> {

    @Modifying
    @Transactional
    @Query(value = "insert into product_user_notification_history (user_id, restock_round, sent_date, product_id) " +
            "values (:userId, :restockRound, now(),:productId)", nativeQuery = true)
    void saveAllTo(Long userId, Long productId, int restockRound);
}
