package com.sparta.task2.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "product", indexes = {
        @Index(name = "idx_product_restock", columnList = "productId, restockRound"),
        @Index(name = "idx_stock_status", columnList = "stockStatus")
})
// 상품테이블
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId; // 상품아이디

    private int restockRound; // 재입고 회차

    private int stockStatus; // 재고 수량

    private String stock; // 재고 상태

    @Version
    private Long version;
}