package com.tm.gogo.web.auth;

import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.term_agreement.Term;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    @Schema(example = "등산 gogo", description = "닉네임", required = true)
    private String nickname;

    @Schema(example = "asdf@naver.net", description = "이메일", required = true)
    private String email;
    @Schema(example = "1q2w3e4r", description = "비밀번호", required = true)
    private String password;

    @Schema(example = "NATIVE", description = "회원 유형", required = true)
    private Member.Type type;

    private final EnumSet<Term> terms = EnumSet.noneOf(Term.class);

    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .nickname(nickname)
                .email(email)
                .password(passwordEncoder.encode(password))
                .type(type)
                .authority(Member.Authority.ROLE_MEMBER)
                .terms(terms)
                .build();
    }
}
