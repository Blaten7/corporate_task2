package com.sparta.task2.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class NotificationRequestDto implements Serializable {

    private Long userId;
    private Long productId;

    public NotificationRequestDto(Long userId, Long productId) {
        this.userId = userId;
        this.productId = productId;
    }

}
