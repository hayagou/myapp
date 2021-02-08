package com.hayagou.myapp.service;

import com.hayagou.myapp.advice.exception.CUserNotFoundException;
import com.hayagou.myapp.entity.User;
import com.hayagou.myapp.model.dto.UserResponseDto;
import com.hayagou.myapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminService {
    final UserRepository userRepository;

    
    //회원 정보 조회
    public HashMap<?,?> getUsers(int page, int size, String key) {

        int totalCount = (int)userRepository.count();

        int totalPage = totalCount / size;

        if (totalCount % size > 0) {
            totalPage++;
        }

        if (totalPage < page) {
            page = totalPage;
        }

        String properties;
        Sort.Direction sort;
        switch (key){
            case "userId":
            case "name":
            case "email":
                properties = key;
                sort = Sort.Direction.DESC;
                break;
            case "-userId":
            case "-name":
            case "-email":
            case "-createdAt":
                properties = key.substring(1);
                sort = Sort.Direction.ASC;
                break;
            default:
                properties = "createdAt";
                sort = Sort.Direction.DESC;
                break;
        }

        Page<User> list = userRepository.findAll(PageRequest.of(page - 1, size, Sort.by(sort, properties)));
        List<UserResponseDto> userList = new ArrayList();

        if(list.isEmpty()){
            throw new CUserNotFoundException();
        }
        for (User user : list){
            userList.add(UserResponseDto.builder().name(user.getName()).email(user.getEmail()).roles(user.getRoles()).createdAt(user.getCreatedAt()).build());
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("list" , userList);
        map.put("totalCount", totalCount);
        map.put("totalPage", totalPage);
        map.put("currentPage", page);
        return map;


    }


//    public ResponseList<PostListDto> getPosts(String boardName, int page, int size) {
//
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
//        response.setMsg(ResponseService.CommonResponse.SUCCESS.getMsg());
//        return response;
//    }
}
