//package com.hayagou.myapp.controller;
//
//import com.hayagou.myapp.model.dto.CommentRequestDto;
//import com.hayagou.myapp.model.dto.CommentResponseDto;
//import com.hayagou.myapp.model.response.ListResponse;
//import com.hayagou.myapp.service.CommentService;
//import com.hayagou.myapp.service.ResponseService;
//import io.swagger.annotations.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//
//@Api(tags = {"6. comment"})
//@RequiredArgsConstructor
//@RestController
//@RequestMapping(value = "/comment")
//public class CommentController {
//    private final ResponseService responseService;
//    private final CommentService commentService;
//
//
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
//    })
//    @ApiOperation(value = "댓글 등록", notes = "댓글을 등록한다.")
//    @PostMapping("/{postId}")
//    public CommonResponse comment(@PathVariable long postId, @Valid @ModelAttribute CommentRequestDto commentRequestDto) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//        commentService.wirteComment(email,postId,commentRequestDto);
//        return responseService.getSuccessResult();
//    }
//
//    @ApiOperation(value = "댓글 목록 조회", notes = "댓글 목록을 조회한다.")
//    @GetMapping("/{postId}")
//    public ListResponse<CommentResponseDto> getComments(@PathVariable long postId, @ApiParam(value = "페이지", defaultValue = "1")@RequestParam int page) {
//        return responseService.getListResult(commentService.getComments(postId, page));
//    }
////
////
////    @ApiImplicitParams({
////            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
////    })
////    @ApiOperation(value = "게시판 삭제", notes = "게시판 삭제한다.")
////    @DeleteMapping
////    public CommonResult deleteBoard(@Valid @ModelAttribute BoardDto boardDto) {
////        boardService.deleteBoard(boardDto);
////        return responseService.getSuccessResult();
////    }
//
//}
