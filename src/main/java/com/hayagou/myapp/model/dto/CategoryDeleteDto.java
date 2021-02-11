package com.hayagou.myapp.model.dto;

import lombok.*;

@Getter
@Setter
public class CategoryDeleteDto extends CategoryResponseDto {
    boolean deleted;
}
