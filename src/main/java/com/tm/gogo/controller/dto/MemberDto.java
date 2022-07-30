package com.tm.gogo.controller.dto;

import com.tm.gogo.domain.Member;
import lombok.Builder;
import lombok.Getter;

public class MemberDto {
    @Getter
    @Builder
    public static class Response {
        private LocationDto location;
        private String nickname;
        private String email;
        private Member.Type type;

        public static Response of(Member member) {
            return Response.builder()
                    .nickname(member.getNickname())
                    .email(member.getEmail())
                    .location(LocationDto.of(member.getLocation()))
                    .type(member.getType())
                    .build();
        }
    }
}
