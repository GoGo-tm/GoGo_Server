package com.tm.gogo.web.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInRequest {

    @Schema(example = "asdf@naver.net", description = "이메일", required = true)
    private String email;

    @Schema(example = "1q2w3e4r", description = "비밀번호", required = true)
    private String password;

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
