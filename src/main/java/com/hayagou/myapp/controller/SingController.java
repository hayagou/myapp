package com.hayagou.myapp.controller;

import com.hayagou.myapp.model.dto.UserInfoDto;
import com.hayagou.myapp.model.response.DataResponse;
import com.hayagou.myapp.service.ResponseService;
import com.hayagou.myapp.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Api(tags = {"1. Sign"})
@RequiredArgsConstructor
@RestController
//@RequestMapping(value = "/user")
public class SingController {

    private final UserService userService;
    private final ResponseService responseService;

    @ApiOperation(value = "이메일 중복 확인", notes = "회원 가입 과정에서 이메일 존재 여부 체크.")
    @GetMapping(value = "/email-check")
    public DataResponse emailCheck(@ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String email) {

        HashMap<String, Boolean> checkResponse = new HashMap<>();
        checkResponse.put("check", userService.emailCheck(email));
        return responseService.getResponse(checkResponse);

    }

    @ApiOperation(value = "로그인", notes = "회원 로그인을 한다.")
    @PostMapping(value = "/login")
    public DataResponse<?> signin(@ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String email,
                                       @ApiParam(value = "비밀번호", required = true) @RequestParam String password) {

        HashMap<String, String> loginResponse = new HashMap<>();
        loginResponse.put("Hello", email + "님 환영합니다");
        loginResponse.put("X-AUTH-TOKEN", userService.signin(email, password));
        return responseService.getResponse(loginResponse);

    }

    @ApiOperation(value = "가입", notes = "회원가입을 한다.")
    @PostMapping(value = "/signup")
    public DataResponse<UserInfoDto> signup(@ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String email,
                                            @ApiParam(value = "비밀번호", required = true) @RequestParam String password,
                                            @ApiParam(value = "이름", required = true) @RequestParam String name) {

       ;
        return responseService.getResponse(userService.singup(email, password, name));
    }


//    @ApiOperation(value = "로그인", notes = "이메일 회원 로그인을 한다.")
//    @PostMapping(value = "/login")
//    public SingleResult<String> signin(@ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String email,
//                                       @ApiParam(value = "비밀번호", required = true) @RequestParam String password) {
//
//        return responseService.getSingleResult(userService.signin(email, password));
//
//    }
//
//    @ApiOperation(value = "가입", notes = "회원가입을 한다.")
//    @PostMapping(value = "/signup")
//    public CommonResult signup(@ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String email,
//                               @ApiParam(value = "비밀번호", required = true) @RequestParam String password,
//                               @ApiParam(value = "이름", required = true) @RequestParam String name) {
//
//        userService.singup(email, password, name);
//        return responseService.getSuccessResult();
//    }
}