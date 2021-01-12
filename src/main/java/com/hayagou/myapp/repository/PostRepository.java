package com.hayagou.myapp.repository;

import com.hayagou.myapp.entity.Board;
import com.hayagou.myapp.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByBoard(Board board , Pageable pageable);

    @Query(
            value = "SELECT p FROM Post p where :#{#board.boardId} = p.board.boardId and p.title like %:keyword% or p.content like %:keyword% or p.author like %:keyword%",
            countQuery = "SELECT count(p.postId) FROM Post p, Board b where :#{#board.boardId} = p.board.boardId and p.title like %:keyword% or p.content like  %:keyword%"
    )
    Page<Post> findAllSearch(@Param("board")Board board, String keyword, Pageable pageable);



}
