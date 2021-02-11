package com.hayagou.myapp.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequestDto {
    private String categoryName;
}
