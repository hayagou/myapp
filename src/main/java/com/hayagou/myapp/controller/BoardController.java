package com.hayagou.myapp.controller;

import com.hayagou.myapp.model.dto.BoardDto;
import com.hayagou.myapp.model.dto.PostRequestDto;
import com.hayagou.myapp.model.response.DataResponse;
import com.hayagou.myapp.model.response.ListResponse;
import com.hayagou.myapp.service.BoardService;
import com.hayagou.myapp.service.PostService;
import com.hayagou.myapp.service.ResponseService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Api(tags = {"3. Board"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/board")
public class BoardController {

    private final PostService postService;
    private final BoardService boardService;
    private final ResponseService responseService;


//    @ApiOperation(value = "게시판 목록 조회", notes = "게시판 목록을 조회한다.")
//    @GetMapping("/test")
//    public ResponseEntity<?> boardTest(@ApiParam(value = "페이지", defaultValue = "1")@RequestParam int page) {
//
//        return ResponseEntity.ok(boardRepository.findById((long) page));
////        return responseService.getListResult(boardService.getBoardList(page));
//    }


    @ApiOperation(value = "게시판 목록 조회", notes = "게시판 목록을 조회한다.")
    @GetMapping
    public DataResponse boards(@ApiParam(value = "Page", defaultValue = "1") @Min(0) @RequestParam int page,
                               @ApiParam(value = "Per Page", defaultValue = "10") @Min(0) @RequestParam int size) {
        return responseService.getResponse(boardService.getBoardList(page, size));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "게시판 등록", notes = "게시판 등록한다.")
    @PostMapping
    public DataResponse board(@Valid @ModelAttribute BoardDto boardDto) {
        return responseService.getResponse(boardService.createBoard(boardDto));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "게시판 삭제", notes = "게시판 삭제한다.")
    @DeleteMapping
    public DataResponse deleteBoard(@Valid @ModelAttribute BoardDto boardDto) {

        return responseService.getResponse(boardService.deleteBoard(boardDto));
    }


//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
//    })
//    @ApiOperation(value = "게시판 삭제", notes = "게시판 삭제한다.")
//    @DeleteMapping
//    public DataResponse deleteBoard(@Valid @ModelAttribute BoardDto boardDto) {
//        boardService.deleteBoard(boardDto);
//        return responseService.getSuccessResult();
//    }

    @ApiOperation(value = "게시판 글 검색 리스트", notes = "게시판 게시글 검색 리스트를 조회한다.")
    @GetMapping(value = "/{boardName}/posts/search")
    public DataResponse posts(@PathVariable String boardName, @ApiParam(value = "keyword") @RequestParam String keyword,
                              @ApiParam(value = "페이지", defaultValue = "1") @RequestParam int page,
                              @ApiParam(value = "PerPage", defaultValue = "10") @Min(0) @RequestParam int size) {
        return responseService.getResponse(postService.getSearchPosts(boardName, keyword, page, size));
    }


    @ApiOperation(value = "게시판 글 리스트", notes = "게시판 게시글 리스트를 조회한다.")
    @GetMapping(value = "/{boardName}/posts")
    public DataResponse posts(@PathVariable String boardName,
                              @ApiParam(value = "페이지", defaultValue = "1") @RequestParam int page,
                              @ApiParam(value = "PerPage", defaultValue = "10") @RequestParam @Min(0) int size) {
        return responseService.getResponse(postService.getPosts(boardName, page, size));
    }

//
//    @ApiOperation(value = "게시판 글 리스트", notes = "게시판 게시글 리스트를 조회한다.")
//    @GetMapping(value = "/{boardName}/posts")
//    public ResponseList<PostListDto> posts(@PathVariable String boardName, @ApiParam(value = "페이지", defaultValue = "1") @Min(0) @RequestParam int page,
//                                           @ApiParam(value = "PerPage", defaultValue = "10") @Min(0) @RequestParam int size) {
//        return postService.getPosts(boardName, page, size);
//    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "게시판 글 작성", notes = "게시판에 글을 작성한다.")
    @PostMapping(value = "/{boardName}")
    public DataResponse post(@PathVariable String boardName, @ModelAttribute PostRequestDto postRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return responseService.getResponse(postService.writePost(email, boardName, postRequestDto));
    }


    @ApiOperation(value = "게시판 글 상세", notes = "게시판 글 상세정보를 조회한다.")
    @GetMapping(value = "/post/{postId}")
    public DataResponse post(@PathVariable long postId) {
        return responseService.getResponse(postService.getPost(postId));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "게시판 글 수정", notes = "게시판의 글을 수정한다.")
    @PutMapping(value = "/post/{postId}")
    public DataResponse post(@PathVariable long postId, @Valid @ModelAttribute PostRequestDto postRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return responseService.getResponse(postService.updatePost(email, postId, postRequestDto));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "게시판 글 삭제", notes = "게시판의 글을 삭제한다.")
    @DeleteMapping(value = "/post/{postId}")
    public DataResponse deletePost(@PathVariable long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return responseService.getResponse(postService.deletePost(email, postId));
    }

}
