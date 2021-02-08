package com.hayagou.myapp.advice;

import com.hayagou.myapp.advice.exception.*;
import com.hayagou.myapp.model.response.ErrorResponse;
import com.hayagou.myapp.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private final ResponseService responseService;

    private final MessageSource messageSource;

    // code정보에 해당하는 메시지를 조회합니다.
    private String getMessage(String code) {
        return getMessage(code, null);
    }
    // code정보, 추가 argument로 현재 locale에 맞는 메시지를 조회합니다.
    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorResponse defaultException(HttpServletRequest request, Exception e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("unKnown.code")), getMessage("unKnown.msg"));
    }

    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorResponse userNotFoundException(HttpServletRequest request, CUserNotFoundException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("userNotFound.code")), getMessage("userNotFound.msg"));
    }

    @ExceptionHandler(CAuthenticationEntryPointException.class)
    public ErrorResponse authenticationEntryPointException(HttpServletRequest request, CAuthenticationEntryPointException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("entryPointException.code")), getMessage("entryPointException.msg"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse AccessDeniedException(HttpServletRequest request, AccessDeniedException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("accessDenied.code")), getMessage("accessDenied.msg"));
    }

    @ExceptionHandler(CNotOwnerException.class)
    @ResponseStatus(HttpStatus.NON_AUTHORITATIVE_INFORMATION)
    public ErrorResponse notOwnerException(HttpServletRequest request, CNotOwnerException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("notOwner.code")), getMessage("notOwner.msg"));
    }

    @ExceptionHandler(CResourceNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse resourceNotExistException(HttpServletRequest request, CResourceNotExistException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("resourceNotExist.code")), getMessage("resourceNotExist.msg"));
    }

//    @ExceptionHandler(CEmailSigninFailedException.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    protected CommonResult emailSigninFailed(HttpServletRequest request, CEmailSigninFailedException e) {
//        return responseService.getFailResult(Integer.valueOf(getMessage("emailSigninFailed.code")), getMessage("emailSigninFailed.msg"));
//    }


    // 모든 예외처리 방식 수정
    @ExceptionHandler(CEmailSigninFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse emailSigninFailed(HttpServletRequest request, CEmailSigninFailedException e) {
//        response = new ErrorResponse(Integer.valueOf(getMessage("emailSigninFailed.code")), getMessage("emailSigninFailed.msg"));

        return responseService.getFailResult(Integer.valueOf(getMessage("emailSigninFailed.code")), getMessage("emailSigninFailed.msg"));
    }

    @ExceptionHandler(DuplicatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse duplicatedException(HttpServletRequest request, DuplicatedException e) {
//        ErrorResponse response = new ErrorResponse(Integer.valueOf(getMessage("duplicatedException.code")), getMessage("duplicatedException.msg"));

        return responseService.getFailResult(Integer.valueOf(getMessage("duplicatedException.code")), getMessage("duplicatedException.msg"));
    }


    @ExceptionHandler(CProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorResponse productNotFound(HttpServletRequest request, CProductNotFoundException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("productNotFoundException.code")), getMessage("productNotFoundException.msg"));
    }

    @ExceptionHandler(CFileException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorResponse fileException(HttpServletRequest request, CFileException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("fileException.code")), getMessage("fileException.msg"));
    }

    @ExceptionHandler(CFileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorResponse fileNotFoundException(HttpServletRequest request, CFileNotFoundException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("fileNotFoundException.code")), getMessage("fileNotFoundException.msg"));
    }

    @ExceptionHandler(CNotCreateDirException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorResponse notCreateDirException(HttpServletRequest request, CNotCreateDirException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("notCreateDirException.code")), getMessage("notCreateDirException.msg"));
    }

    @ExceptionHandler(CInvalidPathException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorResponse invalidPath(HttpServletRequest request, CInvalidPathException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("invalidPath.code")), getMessage("invalidPath.msg"));
    }





}