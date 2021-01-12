package com.hayagou.myapp.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    private String email;
    private String name;
    private LocalDateTime createAt;
}
