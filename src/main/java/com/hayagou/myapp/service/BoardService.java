package com.hayagou.myapp.service;

import com.hayagou.myapp.entity.Board;
import com.hayagou.myapp.model.dto.BoardDto;
import com.hayagou.myapp.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    // 모든 게시판 조회
    @Transactional(readOnly = true)
    public List<BoardDto> getBoardList(int page){
        Page<Board> list = boardRepository.findAll(PageRequest.of(page-1, 10,  Sort.by(Sort.Direction.DESC, "createdAt")));
        List<BoardDto> boardList = new ArrayList<>();
        for (Board board: list) {
            boardList.add(BoardDto.builder().boardName(board.getName()).build());
        }
        return boardList;
    }
    @Transactional
    public Long createBoard(BoardDto boardDto){
        Board board = boardRepository.save(Board.builder().name(boardDto.getBoardName()).build());
        return board.getBoardId();
    }

    @Transactional
    public void deleteBoard(BoardDto boardDto) {
        Board board = boardRepository.findByName(boardDto.getBoardName());
        boardRepository.delete(board);
    }



}
