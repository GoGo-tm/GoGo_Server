package com.tm.gogo.web.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorDto> handleIllegalArgumentException(Exception ex) {
        logger.error(ex);
        ErrorDto errorDto = new ErrorDto(ErrorCode.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<ErrorDto> handleApiException(ApiException ex) {
        logger.error(ex);
        ErrorDto errorDto = new ErrorDto(ex.getErrorCode(), ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus()).body(errorDto);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorDto> handleException(Exception ex) {
        logger.error(ex);
        ErrorDto errorDto = new ErrorDto(ErrorCode.INTERNAL_SERVER_ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }
}
