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

    @Modifying
    @Transactional
    @Query("UPDATE Product P " +
            "SET P.restockRound = P.restockRound + 1, " +
            "P.stockStatus = P.stockStatus + :quantity, " +
            "P.stock = CASE " +
            "WHEN P.stockStatus = 0 THEN 'OUT_OF_STOCK' " +
            "ELSE 'IN_STOCK' END " +
            "WHERE P.productId = :productId")
    void updateRestockRoundAndStockStatusById(@Param("productId") long productId,
                                              @Param("quantity") long quantity);


    @Query("SELECT P.stockStatus from Product P " +
            "WHERE P.productId = :productId")
    int findByIdStockStatus(long productId);

    @Query("select p.restockRound from Product p " +
            "where p.productId = :productId")
    int findByIdRestockRound(Long productId);

//    @Modifying
//    @Query("update Product p " +
//            "set p.stockStatus = p.stockStatus -1 " +
//            "where p.productId = :productId")
//    void updateStockById(long productId);
}
