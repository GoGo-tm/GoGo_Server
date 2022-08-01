package com.tm.gogo.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;
}
