package com.hayagou.myapp.repository;

import com.hayagou.myapp.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAll(Pageable pageable);
    Board findByName(String name);
    Boolean existsBoardByName(String name);
}
