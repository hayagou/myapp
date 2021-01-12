package com.hayagou.myapp.restcontroller;

import com.hayagou.myapp.advice.exception.CEmailSigninFailedException;
import com.hayagou.myapp.config.security.JwtTokenProvider;
import com.hayagou.myapp.entity.User;
import com.hayagou.myapp.model.response.CommonResult;
import com.hayagou.myapp.model.response.SingleResult;
import com.hayagou.myapp.repository.UserRepository;
import com.hayagou.myapp.service.ResponseService;
import com.hayagou.myapp.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@Api(tags = {"1. Sign"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/user")
public class SingRestController {

    private final UserService userService;
    private final ResponseService responseService;

    @ApiOperation(value = "로그인", notes = "이메일 회원 로그인을 한다.")
    @PostMapping(value = "/signin")
    public SingleResult<String> signin(@ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String email,
                                       @ApiParam(value = "비밀번호", required = true) @RequestParam String password) {

        return responseService.getSingleResult(userService.signin(email, password));

    }

    @ApiOperation(value = "가입", notes = "회원가입을 한다.")
    @PostMapping(value = "/signup")
    public CommonResult signup(@ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String email,
                               @ApiParam(value = "비밀번호", required = true) @RequestParam String password,
                               @ApiParam(value = "이름", required = true) @RequestParam String name) {

        userService.singup(email, password, name);
        return responseService.getSuccessResult();
    }
}