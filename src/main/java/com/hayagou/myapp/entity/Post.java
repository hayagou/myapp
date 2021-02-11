package com.hayagou.myapp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Post extends Time {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    @Column(nullable = false, length = 50)
    private String author;
    @Column(nullable = false, length = 100)
    private String title;
    @Column(length = 500)
    private String content;
    private int viewCount;
    private int replyCount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board; // 게시글 - 게시판의 관계 - N:1


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;  // 게시글 - 회원의 관계 - N:1

    // Join 테이블이 Json결과에 표시되지 않도록 처리.
    protected Board getBoard() {
        return board;
    }

    // 생성자
    public Post(User user, Board board,  String title , String content) {
        this.user = user;
        this.board = board;
        this.author = user.getName();
        this.title = title;
        this.content = content;
        this.viewCount = 0;
        this.replyCount = 0;
    }

    // 수정시 데이터 처리
    public Post updatePost(String title, String content) {
        this.title = title;
        this.content = content;
        return this;
    }

    public void updateCount(int count){
        this.viewCount = count + 1;
    }

    public void updateReplyCount(int count){
        this.replyCount = count+1;
    }
}