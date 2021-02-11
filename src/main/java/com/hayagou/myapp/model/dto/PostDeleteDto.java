package com.hayagou.myapp.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
public class PostDeleteDto extends PostResponseDto{
    boolean deleted;
}
