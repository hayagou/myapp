package com.hayagou.myapp.repository;

import com.hayagou.myapp.entity.Board;
import com.hayagou.myapp.service.BoardService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLOutput;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardService boardService;

    @Test
    public void testFindBoard(){
        Board a = boardRepository.save(Board.builder().name("aa").build());

        Board aa = boardRepository.findByName("aa");

        System.out.println(a.getBoardId()+ " " + a.getName());
        System.out.println(aa.getBoardId() + " " + aa.getName());
    }

}