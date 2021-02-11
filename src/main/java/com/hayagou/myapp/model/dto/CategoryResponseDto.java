package com.hayagou.myapp.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDto {
    private String categoryName;
    private Long categoryId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
