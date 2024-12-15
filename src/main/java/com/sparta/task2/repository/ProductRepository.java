package com.sparta.task2.repository;

import com.sparta.task2.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Product p " +
            "SET p.stock = CASE " +
            "WHEN p.stockStatus = 0 THEN 'OUT_OF_STOCK' " +
            "ELSE 'IN_STOCK' END " +
            "WHERE p.productId = :id")
    void updateStockStatusById(@Param("id") long id);
}
