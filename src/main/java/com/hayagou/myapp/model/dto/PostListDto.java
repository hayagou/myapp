package com.hayagou.myapp.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostListDto {
    private Long postId;
    private String title;
    private String author;
    private int viewCount;
    private int replyCount;
    private LocalDateTime createAt;
}
