package com.tm.gogo.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 400
    BAD_REQUEST(HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_NOT_MATCH(HttpStatus.BAD_REQUEST),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST),
    INVALID_TOKEN_VALUE(HttpStatus.BAD_REQUEST),

    // 401
    UNAUTHORIZED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED),

    // 404
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND),

    //408
    REQUEST_TIMEOUT(HttpStatus.REQUEST_TIMEOUT),

    // 409
    ALREADY_EXIST_MEMBER(HttpStatus.CONFLICT),

    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR)
    ;

    private final HttpStatus httpStatus;
}
