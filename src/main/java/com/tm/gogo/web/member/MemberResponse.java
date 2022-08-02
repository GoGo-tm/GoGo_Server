package com.tm.gogo.web.member;

import com.tm.gogo.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponse {
    private LocationDto location;
    private String nickname;
    private String email;
    private Member.Type type;

    public static MemberResponse of(Member member) {
        return MemberResponse.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .location(LocationDto.of(member.getLocation()))
                .type(member.getType())
                .build();
    }
}
