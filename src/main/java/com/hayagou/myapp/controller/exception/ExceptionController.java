package com.hayagou.myapp.controller.exception;

import com.hayagou.myapp.advice.exception.CAuthenticationEntryPointException;
import com.hayagou.myapp.model.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/exception")
public class ExceptionController {

    @GetMapping(value = "/entrypoint")
    public ErrorResponse entrypointException() {
        throw new CAuthenticationEntryPointException();
    }
    @GetMapping(value = "/accessdenied")
    public ErrorResponse accessdeniedException() {
        throw new AccessDeniedException("");
    }
}