package com.tm.gogo.web.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequest {
    @Schema(example = "등산 gogo", description = "닉네임", required = true)
    private String nickname;

    @Schema(example = "asdf@naver.net", description = "이메일", required = true)
    private String email;

    @Schema(example = "1q2w3e4r", description = "비밀번호", required = true)
    private String password;

    @Schema(example = "1q2w3e4r", description = "비밀번호", required = true)
    private String newPassword;

    @Schema(example = "true", description = "위치정보 이용 동의")
    private boolean agreed;
}
