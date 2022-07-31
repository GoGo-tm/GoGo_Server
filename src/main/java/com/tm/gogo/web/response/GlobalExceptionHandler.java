package com.tm.gogo.web.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseDto<Void> handleIllegalArgumentException(Exception ex) {
        return ResponseDto.fail(ErrorCode.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseDto<Void> handleException(Exception ex) {
        return ResponseDto.fail(ErrorCode.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}
