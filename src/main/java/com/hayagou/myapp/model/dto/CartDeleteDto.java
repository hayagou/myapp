package com.hayagou.myapp.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDeleteDto extends CartResponseDto{
    boolean deleted;
}
