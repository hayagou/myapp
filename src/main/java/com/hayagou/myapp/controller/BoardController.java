package com.hayagou.myapp.controller;

import com.hayagou.myapp.model.dto.BoardDto;
import com.hayagou.myapp.model.dto.PostListDto;
import com.hayagou.myapp.model.dto.PostRequestDto;
import com.hayagou.myapp.model.dto.PostResponseDto;
import com.hayagou.myapp.model.response.CommonResult;
import com.hayagou.myapp.model.response.ListResult;
import com.hayagou.myapp.model.response.SingleResult;
import com.hayagou.myapp.service.BoardService;
import com.hayagou.myapp.service.PostService;
import com.hayagou.myapp.service.ResponseService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = {"3. Board"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/board")
public class BoardController {

    private final PostService postService;
    private final BoardService boardService;
    private final ResponseService responseService;

    @ApiOperation(value = "게시판 목록 조회", notes = "게시판 목록을 조회한다.")
    @GetMapping
    public ListResult<BoardDto> boards( @ApiParam(value = "페이지", defaultValue = "1")@RequestParam int page) {
        return responseService.getListResult(boardService.getBoardList(page));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "게시판 등록", notes = "게시판 등록한다.")
    @PostMapping
    public SingleResult<Long> board(@Valid @ModelAttribute BoardDto boardDto) {
        return responseService.getSingleResult(boardService.createBoard(boardDto));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "게시판 삭제", notes = "게시판 삭제한다.")
    @DeleteMapping
    public CommonResult deleteBoard(@Valid @ModelAttribute BoardDto boardDto) {
        boardService.deleteBoard(boardDto);
        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "게시판 글 검색 리스트", notes = "게시판 게시글 검색 리스트를 조회한다.")
    @GetMapping(value = "/{boardName}/posts/{keyword}")
    public ListResult<PostListDto> posts(@PathVariable String boardName, @PathVariable String keyword,  @ApiParam(value = "페이지", defaultValue = "1")@RequestParam int page) {
        return responseService.getListResult(postService.getSearchPosts(boardName, keyword, page));
    }


    @ApiOperation(value = "게시판 글 리스트", notes = "게시판 게시글 리스트를 조회한다.")
    @GetMapping(value = "/{boardName}/posts")
    public ListResult<PostListDto> posts(@PathVariable String boardName, @ApiParam(value = "페이지", defaultValue = "1")@RequestParam int page) {
        return responseService.getListResult(postService.getPosts(boardName, page));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "게시판 글 작성", notes = "게시판에 글을 작성한다.")
    @PostMapping(value = "/{boardName}")
    public SingleResult<Long> post(@PathVariable String boardName, @ModelAttribute PostRequestDto postRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return responseService.getSingleResult(postService.writePost(email, boardName, postRequestDto));
    }



    @ApiOperation(value = "게시판 글 상세", notes = "게시판 글 상세정보를 조회한다.")
    @GetMapping(value = "/post/{postId}")
    public SingleResult<PostResponseDto> post(@PathVariable long postId) {
        return responseService.getSingleResult(postService.getPost(postId));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "게시판 글 수정", notes = "게시판의 글을 수정한다.")
    @PutMapping(value = "/post/{postId}")
    public SingleResult<PostResponseDto> post(@PathVariable long postId, @Valid @ModelAttribute PostRequestDto postRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return responseService.getSingleResult(postService.updatePost(email, postId, postRequestDto));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "게시판 글 삭제", notes = "게시판의 글을 삭제한다.")
    @DeleteMapping(value = "/post/{postId}")
    public CommonResult deletePost(@PathVariable long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        postService.deletePost(email, postId);
        return responseService.getSuccessResult();
    }

}
