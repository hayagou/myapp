package com.hayagou.myapp.service;

import com.hayagou.myapp.advice.exception.CResourceNotExistException;
import com.hayagou.myapp.advice.exception.CUserNotFoundException;
import com.hayagou.myapp.advice.exception.DuplicatedException;
import com.hayagou.myapp.entity.Board;
import com.hayagou.myapp.entity.User;
import com.hayagou.myapp.model.dto.*;
import com.hayagou.myapp.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;


    public PaginationDto getBoardList(int page, int size) {


        int totalCount = (int) boardRepository.count();

        // 데이터가 존재 하지 않을 경우
        if(totalCount == 0){
            throw new CResourceNotExistException();
        }

        int totalPage = totalCount / size;

        if (totalCount % size > 0) {
            totalPage++;
        }

        if (totalPage < page) {
            page = totalPage;
        }

//        String properties;
//        Sort.Direction sort;
//        switch (key){
//            case "userId":
//            case "name":
//            case "email":
//                properties = key;
//                sort = Sort.Direction.DESC;
//                break;
//            case "-userId":
//            case "-name":
//            case "-email":
//            case "-createdAt":
//                properties = key.substring(1);
//                sort = Sort.Direction.ASC;
//                break;
//            default:
//                properties = "createdAt";
//                sort = Sort.Direction.DESC;
//                break;
//        }


        Page<Board> list = boardRepository.findAll(PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        List<BoardDto> boardList = new ArrayList<>();
        for (Board board : list) {
            boardList.add(BoardDto.builder().boardName(board.getName()).build());
        }

//        PaginationDto<?> paginationDto = PaginationDto.builder().totalPage(totalPage).totalPage(totalPage).currentPage(page).items(Collections.singletonList(userList)).build();

        PaginationDto paginationDto = PaginationDto.builder().totalPage(totalPage).totalCount(totalCount).currentPage(page).items(Collections.singletonList(boardList)).build();
        return paginationDto;

    }
    // 모든 게시판 조회
//    @Transactional(readOnly = true)
//    public List<BoardDto> getBoardList(int page){
//        Page<Board> list = boardRepository.findAll(PageRequest.of(page-1, 10,  Sort.by(Sort.Direction.DESC, "createdAt")));
//        List<BoardDto> boardList = new ArrayList<>();
//        for (Board board: list) {
//            boardList.add(BoardDto.builder().boardName(board.getName()).build());
//        }
//        return boardList;
//    }


    @Transactional
    public BoardResponseDto createBoard(BoardDto boardDto) {
        if (boardRepository.existsBoardByName(boardDto.getBoardName())){
            throw new DuplicatedException();
        }

        Board board = boardRepository.save(Board.builder().name(boardDto.getBoardName()).build());

        BoardResponseDto boardResponseDto = new BoardResponseDto();

        boardResponseDto.setBoardId(board.getBoardId());
        boardResponseDto.setCreatedAt(board.getCreatedAt());
        boardResponseDto.setBoardName(board.getName());


        return boardResponseDto;
    }

    @Transactional
    public BoardDto deleteBoard(BoardDto boardDto) {
        if(!boardRepository.existsBoardByName(boardDto.getBoardName())){
            throw new CResourceNotExistException();
        }

        Board board = boardRepository.findByName(boardDto.getBoardName());
        ;

        BoardDeleteDto boardDeleteDto = new BoardDeleteDto();

        boardDeleteDto.setBoardId(board.getBoardId());
        boardDeleteDto.setBoardName(board.getName());
        boardDeleteDto.setCreatedAt(board.getCreatedAt());

        boardRepository.delete(board);
        boardDeleteDto.setDeleted(true);


        return boardDeleteDto;
    }


}
