package com.hayagou.myapp.service;

import com.hayagou.myapp.entity.Board;
import com.hayagou.myapp.model.dto.BoardDto;
import com.hayagou.myapp.model.dto.PostRequestDto;
import com.hayagou.myapp.repository.BoardRepository;
import com.hayagou.myapp.repository.PostRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostServiceTest {
    @Autowired
    private PostService postService;

    @Autowired
    private BoardService boardService;
    @Autowired
    private UserService userService;

    @Autowired
    private PostRepository postRepository;

    @Test
    public void boardPageTest(){
        userService.singup("aa","aa","aa");
        boardService.createBoard(BoardDto.builder().boardName("aaa").build());
        PostRequestDto postRequestDto = PostRequestDto.builder().content("aa").title("aa").build();

        for(int i= 0; i<100 ; i++){
            postService.writePost("aa", "aaa",postRequestDto);
        }
        postService.writePost("aa", "aaa", PostRequestDto.builder().title("b").content("bbb").build());
        postRepository.findAllSearch(postService.findBoard("aaa"),"aa" , PageRequest.of(0, 10,  Sort.by(Sort.Direction.DESC, "createdAt")));
//        postService.getSearchPosts("aaa", "b", 1);
    }
}