package com.hayagou.myapp.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class BoardResponseDto extends BoardDto{
    private Long boardId;
    private LocalDateTime createdAt;
}
