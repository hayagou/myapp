package com.hayagou.myapp.model.dto;

import com.hayagou.myapp.entity.Post;
import lombok.*;

import java.time.LocalDateTime;

// 게시글 출력할때
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Long postId;
    private String title;
    private String content;
    private String author;
    private int viewCount;
    private int replyCount;
    private LocalDateTime createdAt;

//    public Post toEntity(){
//        return Post.builder()
//    }
}
