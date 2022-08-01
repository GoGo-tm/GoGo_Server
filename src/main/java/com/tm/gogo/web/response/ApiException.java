package com.tm.gogo.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ApiException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public HttpStatus getHttpStatus() {
        return errorCode.getHttpStatus();
    }
}
