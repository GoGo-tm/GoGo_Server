package com.tm.gogo.web.auth;


import com.tm.gogo.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpResponse {

    @Schema(example = "asdf@naver.net", description = "가입 완료된 계정 이메일", required = true)
    private String email;

    public static SignUpResponse of(Member member) {
        return new SignUpResponse(member.getEmail());
    }
}
