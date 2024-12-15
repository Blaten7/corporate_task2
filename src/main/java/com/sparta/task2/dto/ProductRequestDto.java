package com.sparta.task2.dto;

import lombok.Data;

@Data
public class ProductRequestDto {
    private Long productId;
    private int restockRound;
}
