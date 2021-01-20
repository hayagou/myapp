package com.hayagou.myapp.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderResponseDto {
    private Long orderId;
    private String productName;
    private float price;
    private String userName;
    private LocalDateTime createAt;
}
