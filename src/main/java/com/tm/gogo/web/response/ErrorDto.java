package com.tm.gogo.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {

    @Schema(example = "INTERNAL_SERVER_ERROR", description = "에러 종류 구분 가능한 코드")
    private ErrorCode code;

    @Schema(example = "서버에 문제가 생겼습니다.", description = "에러 원인 파악용 추가 메시지")
    private String message;
}
