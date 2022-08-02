package com.tm.gogo.web.member;

import com.tm.gogo.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponse {

    @Schema(example = "등산 gogo")
    private String nickname;

    @Schema(example = "asdf@naver.net")
    private String email;

    @Schema(example = "NATIVE")
    private Member.Type type;

    public static MemberResponse of(Member member) {
        return MemberResponse.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .type(member.getType())
                .build();
    }
}
