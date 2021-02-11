package com.hayagou.myapp.service;

import com.hayagou.myapp.advice.exception.CNotOwnerException;
import com.hayagou.myapp.advice.exception.CResourceNotExistException;
import com.hayagou.myapp.advice.exception.CUserNotFoundException;
import com.hayagou.myapp.entity.Comment;
import com.hayagou.myapp.entity.Post;
import com.hayagou.myapp.entity.User;
import com.hayagou.myapp.model.dto.CommentRequestDto;
import com.hayagou.myapp.model.dto.CommentResponseDto;
import com.hayagou.myapp.model.dto.PaginationDto;
import com.hayagou.myapp.repository.CommentRepository;
import com.hayagou.myapp.repository.PostRepository;
import com.hayagou.myapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class CommentService {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    // write
    @Transactional
    public CommentResponseDto wirteComment(String email, Long postId , CommentRequestDto commentRequestDto){
        User user = userRepository.findByEmail(email).orElseThrow(CUserNotFoundException::new);
        Post post = postRepository.findById(postId).orElseThrow(CResourceNotExistException::new);
        post.updateReplyCount(post.getReplyCount());

        Comment comment = Comment.builder().content(commentRequestDto.getContent()).post(post).user(user).build();
        commentRepository.save(comment);
        return CommentResponseDto.builder().commentId(comment.getCommentId()).userName(comment.getUser().getName()).content(comment.getContent()).createAt(comment.getCreatedAt()).build();
    }
    // get comment list by post id
    @Transactional
    public PaginationDto getComments(Long postId, int page, int size){

        Post post = postRepository.findById(postId).orElseThrow(CResourceNotExistException::new);

        int totalCount = post.getReplyCount();

        int totalPage = totalCount / size;

        if (totalCount % size > 0) {
            totalPage++;
        }

        if (totalPage < page) {
            page = totalPage;
        }


        Page<Comment> list = commentRepository.findAllByPost(post, PageRequest.of(page-1, size,  Sort.by(Sort.Direction.DESC, "createdAt")));
        List<CommentResponseDto> commentDtoList = new ArrayList<>();
        for (Comment comment: list) {
            commentDtoList.add(CommentResponseDto.builder().commentId(comment.getCommentId()).userName(comment.getUser().getName()).content(comment.getContent()).createAt(comment.getCreatedAt()).build());
        }

        PaginationDto paginationDto = PaginationDto.builder().totalCount(totalCount).currentPage(page).totalPage(totalPage).items(Collections.singletonList(commentDtoList)).build();
        return paginationDto;
    }
    // del

    public void deleteComment(String email, Long commentId){
        Comment comment = commentRepository.findById(commentId).orElseThrow(CResourceNotExistException::new);
        if(!email.equals(comment.getUser().getEmail())){
            throw new CNotOwnerException();
        }
        commentRepository.delete(comment);
    }


    // update

}
