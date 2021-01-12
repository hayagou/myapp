package com.hayagou.myapp.service;

import com.hayagou.myapp.model.dto.BoardDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private PostService postService;


    @Test
    public void boardPageTest(){
        boardService.createBoard(BoardDto.builder().boardName("aaa").build());
        postService.findBoard("aaa");
        System.out.println(boardService.getBoardList(0).get(0).toString());
    }
}