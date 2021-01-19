package com.hayagou.myapp.controller;

import com.hayagou.myapp.model.dto.UserInfoDto;
import com.hayagou.myapp.model.dto.UserUpdateDto;
import com.hayagou.myapp.model.response.CommonResult;
import com.hayagou.myapp.model.response.ListResult;
import com.hayagou.myapp.model.response.SingleResult;
import com.hayagou.myapp.service.ResponseService;
import com.hayagou.myapp.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"2. User"})
@RequiredArgsConstructor
@RestController
@RequestMapping
public class UserController {

    private final UserService userService;
    private final ResponseService responseService; // 결과를 처리할 Service

    @ApiOperation(value = "회원 리스트 조회", notes = "모든 회원을 조회한다")
    @GetMapping(value = "/users")
    public ListResult<UserInfoDto> findAllUser(@ApiParam(value = "페이지", defaultValue = "1") @RequestParam int page) {
        // 결과데이터가 여러건인경우 getListResult를 이용해서 결과를 출력한다.
        return responseService.getListResult(userService.getUsers(page));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 단건 조회", notes = "자신의 정보를 조회한다")
    @GetMapping(value = "/user")
    public SingleResult<UserInfoDto> findUserById() {
        // 결과데이터가 단일건인경우 getBasicResult를 이용해서 결과를 출력한다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return responseService.getSingleResult(userService.getUser(email));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 수정", notes = "회원정보를 수정한다")
    @PutMapping(value = "/user")
    public CommonResult modify(
            @ApiParam(value = "회원정보", required = true) @ModelAttribute UserUpdateDto userUpdateDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        userService.updateUser(email, userUpdateDto);

        return responseService.getSuccessResult();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 탈퇴", notes = "회원정보를 삭제한다")
    @DeleteMapping(value = "/user")
    public CommonResult delete() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        userService.deleteUser(email);
        // 성공 결과 정보만 필요한경우 getSuccessResult()를 이용하여 결과를 출력한다.
        return responseService.getSuccessResult();
    }
    //유저 정보 조회

    // 유저 정보 수정

    // 회원 탈퇴

    //유저 비밀번호 변경
}
