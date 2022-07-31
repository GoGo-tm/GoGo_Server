package com.tm.gogo.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto<T> {
    private Boolean success;
    private T data;
    private ErrorDto error;

    public ResponseDto(Boolean success) {
        this.success = success;
    }

    public ResponseDto(Boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public ResponseDto(Boolean success, ErrorDto error) {
        this.success = success;
        this.error = error;
    }

    public static ResponseDto<Void> ok() {
        return new ResponseDto<>(true);
    }

    public static <T> ResponseDto<T> ok(T result) {
        return new ResponseDto<>(true, result);
    }

    public static ResponseDto<Void> fail(ErrorCode code, String message) {
        return new ResponseDto<>(false, new ErrorDto(code, message));
    }
}
