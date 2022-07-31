package com.tm.gogo.web.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseDto<Void> handleIllegalArgumentException(Exception ex) {
        logger.error(ex);
        return ResponseDto.fail(ErrorCode.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseDto<Void> handleNoSuchElementException(Exception ex) {
        logger.error(ex);
        return ResponseDto.fail(ErrorCode.NOT_FOUND, ex.getMessage());
    }
    @ExceptionHandler(Exception.class)
    protected ResponseDto<Void> handleException(Exception ex) {
        logger.error(ex);
        return ResponseDto.fail(ErrorCode.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}
