package com.hayagou.myapp.service;

import com.hayagou.myapp.advice.exception.CNotOwnerException;
import com.hayagou.myapp.advice.exception.CResourceNotExistException;
import com.hayagou.myapp.advice.exception.CUserNotFoundException;
import com.hayagou.myapp.entity.Board;
import com.hayagou.myapp.entity.Post;
import com.hayagou.myapp.entity.User;
import com.hayagou.myapp.model.dto.*;
import com.hayagou.myapp.model.response.ListResponse;
import com.hayagou.myapp.repository.BoardRepository;
import com.hayagou.myapp.repository.PostRepository;
import com.hayagou.myapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 게시판 이름으로 게시판을 조회. 없을경우 CResourceNotExistException 처리
    @Transactional(readOnly = true)
    public Board findBoard(String boardName) {
        return Optional.ofNullable(boardRepository.findByName(boardName)).orElseThrow(CResourceNotExistException::new);
    }

    // 게시물을 등록합니다. 게시물의 회원UID가 조회되지 않으면 CUserNotFoundException 처리합니다.
    @Transactional
    public PostResponseDto writePost(String email, String boardName, PostRequestDto postRequestDto) {
        Board board = findBoard(boardName);
        Post post = new Post(userRepository.findByEmail(email).orElseThrow(CUserNotFoundException::new), board, postRequestDto.getTitle(), postRequestDto.getContent());

        Post savedPost = postRepository.save(post);
        PostResponseDto postResponseDto = PostResponseDto.builder()
                .author(savedPost.getAuthor())
                .postId(savedPost.getPostId())
                .content(savedPost.getContent())
                .createdAt(savedPost.getCreatedAt())
                .replyCount(savedPost.getReplyCount())
                .title(savedPost.getTitle())
                .viewCount(savedPost.getViewCount())
                .build();
        return postResponseDto;
    }

    @Transactional
    public PostResponseDto getPost(long postId) {
        Post post = postRepository.findById(postId).orElseThrow(CResourceNotExistException::new);
        post.updateCount(post.getViewCount());
        PostResponseDto postResponseDto = PostResponseDto.builder().title(post.getTitle()).content(post.getContent()).author(post.getAuthor()).postId(post.getPostId())
                .createdAt(post.getCreatedAt()).viewCount(post.getViewCount()).build();
        return postResponseDto;
    }

    @Transactional(readOnly = true)
    public PaginationDto getPosts(String boardName, int page, int size) {


        Board board = findBoard(boardName);

        int totalCount = (int) postRepository.countAllByBoard(board);

        int totalPage = totalCount / size;

        if (totalCount % size > 0) {
            totalPage++;
        }

        if (totalPage < page) {
            page = totalPage;
        }



        Page<Post> list = postRepository.findByBoard(board, PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "createdAt")));
        List<PostResponseDto> postList = new ArrayList<>();
        for (Post post : list) {
            postList.add(PostResponseDto.builder().title(post.getTitle()).postId(post.getPostId())
                    .author(post.getAuthor()).viewCount(post.getViewCount()).createdAt(post.getCreatedAt()).build());
        }


        PaginationDto paginationDto = PaginationDto.builder()
                .totalPage(totalPage)
                .totalCount(totalCount)
                .currentPage(page)
                .items(Collections.singletonList(postList)).build();

        return paginationDto;
    }

//    @Transactional(readOnly = true)
//    public List<PostListDto> getPosts(String boardName, int page) {
//
//
//        Board board = findBoard(boardName);
//        Page<Post> list = postRepository.findByBoard(board, PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "createdAt")));
//        List<PostListDto> postList = new ArrayList<>();
//        for (Post post : list) {
//            postList.add(PostListDto.builder().title(post.getTitle()).postId(post.getPostId())
//                    .author(post.getAuthor()).viewCount(post.getViewCount()).createdAt(post.getCreatedAt()).build());
//        }
//        return postList;
//    }

//    @Transactional(readOnly = true)
//    public Map<String, Object> getPostsTest(String boardName, int page){
//
//
//        Board board = findBoard(boardName);
//        Page<Post> list = postRepository.findByBoard(board , PageRequest.of(page-1, 10,  Sort.by(Sort.Direction.DESC, "createdAt")));
//        List<PostListDto> postList = new ArrayList<>();
//        for (Post post: list) {
//            postList.add(PostListDto.builder().title(post.getTitle()).postId(post.getPostId())
//                    .author(post.getAuthor()).viewCount(post.getViewCount()).createdAt(post.getCreatedAt()).build());
//        }
//
//        int count = postRepository.countAllByBoard(board);
//        Map<String, Object> map = new HashMap<>();
//        map.put("totalCount", count);
//        map.put("list", postList);
//        return map;
//    }

    @Transactional
    public PostResponseDto updatePost(String email, long postId, PostRequestDto postRequestDto) {
        Post post = postRepository.findById(postId).orElseThrow(CResourceNotExistException::new);
        User user = post.getUser();
        if (!email.equals(user.getEmail()))
            throw new CNotOwnerException();
        post.updatePost(postRequestDto.getTitle(), postRequestDto.getContent());

        return getPost(postId);

    }

    // 게시물을 삭제합니다. 게시물 등록자와 로그인 회원정보가 틀리면 CNotOwnerException 처리합니다.
    @Transactional
    public PostResponseDto deletePost(String email, long postId) {
        Post post = postRepository.findById(postId).orElseThrow(CResourceNotExistException::new);
        User user = post.getUser();
        if (!email.equals(user.getEmail()))
            throw new CNotOwnerException();

        PostDeleteDto postDeleteDto = new PostDeleteDto();
        postDeleteDto.setTitle(post.getTitle());
        postDeleteDto.setContent(post.getContent());
        postDeleteDto.setAuthor(post.getAuthor());
        postDeleteDto.setPostId(post.getPostId());
        postDeleteDto.setCreatedAt(post.getCreatedAt());
        postDeleteDto.setViewCount(post.getViewCount());
        postDeleteDto.setReplyCount(post.getReplyCount());
        postRepository.delete(post);
        postDeleteDto.setDeleted(true);
        return postDeleteDto;
    }

    @Transactional
    public PaginationDto getSearchPosts(String boardName, String keyword, int page, int size) {

//        int totalCount = (int) postRepository.count();

        Board board = findBoard(boardName);
        int totalCount = postRepository.countAllSearch(board, keyword);
        int totalPage = totalCount / size;

        if (totalCount % size > 0) {
            totalPage++;
        }

        if (totalPage < page) {
            page = totalPage;
        }


        Page<Post> list = postRepository.findAllSearch(board, keyword, PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        List<PostListDto> postList = new ArrayList<>();
        for (Post post : list) {
            postList.add(PostListDto.builder().title(post.getTitle()).postId(post.getPostId())
                    .author(post.getAuthor()).viewCount(post.getViewCount()).createdAt(post.getCreatedAt()).build());
        }

        PaginationDto paginationDto = PaginationDto.builder().currentPage(page).totalPage(totalPage).totalCount(totalCount).items(Collections.singletonList(postList)).build();


        return paginationDto;
    }


//    //    Angular에서 페이징처리를 위한 게시글 조회 and RestFul하게 다시 설계
//    @Transactional(readOnly = true)
//    public ListResponse<PostListDto> getPosts(String boardName, int page, int size) {
//        Board board = findBoard(boardName);
//        int totalCount = postRepository.countAllByBoard(board);
//
//        int totalPage = totalCount / size;
//
//        if (totalCount % size > 0) {
//            totalPage++;
//        }
//
//        if (totalPage < page) {
//            page = totalPage;
//        }
//
//        Page<Post> list = postRepository.findByBoard(board, PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt")));
//        List<PostListDto> postList = new ArrayList<>();
//        for (Post post : list) {
//            postList.add(PostListDto.builder().title(post.getTitle()).postId(post.getPostId())
//                    .author(post.getAuthor()).viewCount(post.getViewCount()).createdAt(post.getCreatedAt()).build());
//        }
//        ResponseList<PostListDto> response = new ResponseList<>();
//        response.setList(postList);
//        response.setCurrentPage(page);
//        response.setTotalCount(totalCount);
//        response.setTotalPage(totalPage);
////        response.setMsg(ResponseService.Response.SUCCESS.getMsg());
//        return response;
//    }
//
//
//    @Transactional
//    public ListResponse<PostListDto> getSearchPosts(String boardName, String keyword, int page, int size) {
//        Board board = findBoard(boardName);
//
//        int totalCount = postRepository.countAllSearch(board, keyword);
//
//        int totalPage = totalCount / size;
//
//        if (totalCount % size > 0) {
//            totalPage++;
//        }
//
//        if (totalPage < page) {
//            page = totalPage;
//        }
//
//
//        Page<Post> list = postRepository.findAllSearch(board, keyword, PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt")));
//        List<PostListDto> postList = new ArrayList<>();
//        for (Post post : list) {
//            postList.add(PostListDto.builder().title(post.getTitle()).postId(post.getPostId())
//                    .author(post.getAuthor()).viewCount(post.getViewCount()).createdAt(post.getCreatedAt()).build());
//        }
//
//        ResponseList<PostListDto> response = new ResponseList<>();
//        response.setList(postList);
//        response.setCurrentPage(page);
//        response.setTotalCount(totalCount);
//        response.setTotalPage(totalPage);
////        response.setMsg(ResponseService.Response.SUCCESS.getMsg());
//        return response;
//    }
}
