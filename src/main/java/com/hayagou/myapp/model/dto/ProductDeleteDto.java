package com.hayagou.myapp.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDeleteDto extends ProductResponseDto {
    private boolean deleted;
}
