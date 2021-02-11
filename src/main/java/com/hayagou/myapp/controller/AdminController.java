package com.hayagou.myapp.controller;

import com.hayagou.myapp.service.AdminService;
import com.hayagou.myapp.service.ResponseService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@Api(tags = {"10. Admin"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/admin")
public class AdminController {

    final AdminService adminService;
    final ResponseService responseService;

    // 전체 회원 조회
    @ApiOperation(value = "회원 리스트 조회", notes = "key : [userId, name, eamil, createdAt(default)], " + "오름차순: -[key] ex) key=-createdAt")
    @GetMapping(value = "/users")
    public ResponseEntity<?> findAllUser(
            @ApiParam(value = "Page", defaultValue = "1") @Min(0) @RequestParam int page,
            @ApiParam(value = "Per Page", defaultValue = "10") @Min(0) @RequestParam int size,
            @ApiParam(value = "조회 유형", defaultValue = "createdAt") @Min(0) @RequestParam String key) {

        return ResponseEntity.ok(responseService.getResponse((adminService.getUsers(page, size, key))));
    }

//    // 특정 회원 조회
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
//    })
//    @ApiOperation(value = "회원 단건 조회", notes = "자신의 정보를 조회한다")
//    @GetMapping(value = "/user/{}")
//    public SingleResponse<UserInfoDto> findUserById() {
//        // 결과데이터가 단일건인경우 getBasicResult를 이용해서 결과를 출력한다.
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//        return responseService.getSingleResult(userService.getUser(email));
//    }
}
