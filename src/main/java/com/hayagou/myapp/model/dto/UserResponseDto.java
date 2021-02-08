package com.hayagou.myapp.model.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private String email;
    private String name;
    private LocalDateTime createdAt;
    private List<String> roles;
}
