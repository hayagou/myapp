package com.hayagou.myapp.service;

import com.hayagou.myapp.advice.exception.CNotOwnerException;
import com.hayagou.myapp.advice.exception.CResourceNotExistException;
import com.hayagou.myapp.advice.exception.CUserNotFoundException;
import com.hayagou.myapp.entity.Board;
import com.hayagou.myapp.entity.Post;
import com.hayagou.myapp.entity.User;
import com.hayagou.myapp.model.dto.PostListDto;
import com.hayagou.myapp.model.dto.PostRequestDto;
import com.hayagou.myapp.model.dto.PostResponseDto;
import com.hayagou.myapp.model.redis.CacheKey;
import com.hayagou.myapp.repository.BoardRepository;
import com.hayagou.myapp.repository.PostRepository;
import com.hayagou.myapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 게시판 이름으로 게시판을 조회. 없을경우 CResourceNotExistException 처리
    @Transactional(readOnly = true)
    @Cacheable(value = CacheKey.BOARD, key = "#boardName", unless ="#result == null")
    public Board findBoard(String boardName) {
        return Optional.ofNullable(boardRepository.findByName(boardName)).orElseThrow(CResourceNotExistException::new);
    }
    // 게시물을 등록합니다. 게시물의 회원UID가 조회되지 않으면 CUserNotFoundException 처리합니다.
    @Transactional
    public Long writePost(String email, String boardName, PostRequestDto postRequestDto) {
        Board board = findBoard(boardName);
        Post post = new Post(userRepository.findByEmail(email).orElseThrow(CUserNotFoundException::new), board , postRequestDto.getTitle(), postRequestDto.getContent());
       ;
        return  postRepository.save(post).getPostId();
    }

    @Transactional
    public PostResponseDto getPost(long postId){
        Post post = postRepository.findById(postId).orElseThrow(CResourceNotExistException::new);
        post.updateCount(post.getViewCount());
        PostResponseDto postResponseDto = PostResponseDto.builder().title(post.getTitle()).content(post.getContent()).author(post.getAuthor()).postId(post.getPostId())
                .createAt(post.getCreatedAt()).viewCount(post.getViewCount()).build();
        return postResponseDto;
    }

    @Transactional(readOnly = true)
    public List<PostListDto> getPosts(String boardName, int page){
        Board board = findBoard(boardName);
        Page<Post> list = postRepository.findByBoard(board , PageRequest.of(page-1, 10,  Sort.by(Sort.Direction.DESC, "createdAt")));
        List<PostListDto> postList = new ArrayList<>();
        for (Post post: list) {
            postList.add(PostListDto.builder().title(post.getTitle()).postId(post.getPostId())
                    .author(post.getAuthor()).viewCount(post.getViewCount()).createAt(post.getCreatedAt()).build());
        }
        return postList;
    }

    @Transactional
    public PostResponseDto updatePost(String email, long postId, PostRequestDto postRequestDto){
        Post post = postRepository.findById(postId).orElseThrow(CResourceNotExistException::new);
        User user = post.getUser();
        if (!email.equals(user.getEmail()))
            throw new CNotOwnerException();
        post.updatePost(postRequestDto.getTitle(), postRequestDto.getContent());

        return getPost(postId);

    }

    // 게시물을 삭제합니다. 게시물 등록자와 로그인 회원정보가 틀리면 CNotOwnerException 처리합니다.
    @Transactional
    public boolean deletePost(String email, long postId) {
        Post post = postRepository.findById(postId).orElseThrow(CResourceNotExistException::new);
        User user = post.getUser();
        if (!email.equals(user.getEmail()))
            throw new CNotOwnerException();
        postRepository.delete(post);
        return true;
    }

    @Transactional
    public List<PostListDto> getSearchPosts(String boardName, String keyword, int page){
        Board board = findBoard(boardName);
        Page<Post> list = postRepository.findAllSearch(board, keyword , PageRequest.of(page-1, 10,  Sort.by(Sort.Direction.DESC, "createdAt")));
        List<PostListDto> postList = new ArrayList<>();
        for (Post post: list) {
            postList.add(PostListDto.builder().title(post.getTitle()).postId(post.getPostId())
                    .author(post.getAuthor()).viewCount(post.getViewCount()).createAt(post.getCreatedAt()).build());
        }
        return postList;
    }

}
