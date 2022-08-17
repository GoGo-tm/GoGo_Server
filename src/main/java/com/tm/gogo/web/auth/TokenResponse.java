package com.tm.gogo.web.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponse {
    @Schema(example = "Bearer")
    private String grantType;

    @Schema(example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIzIiwiYXV0aCI6IlJPTEVfTUVNQkVSIiwiZXhwIjoxNjU5NDc2MzgxfQ.pFZpYM77Appl2y6atPFV2oWRPKmMQWqgLVdp9boCT5kr7xibIgvCQoNGwx6V1qMupNYUuSgeCKYH2SjkU_gCAg")
    private String accessToken;

    @Schema(example = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2NjAwNzkzODF9.VS9dbKWi2XNdo6F0Yz1PdJuD1TjxFAUFgj5c65HAWtyOsfSA0ASIvA6SlCh5HqJqdGKsrux72SxrhxsZg05C-g")
    private String refreshToken;

    @Schema(example = "1659476381416", description = "Access Token 만료 시간")
    private Long accessTokenExpiresIn;
}
