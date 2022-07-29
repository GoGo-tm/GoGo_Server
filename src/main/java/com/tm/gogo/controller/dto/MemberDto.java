package com.tm.gogo.controller.dto;

import com.tm.gogo.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MemberDto {
    @Getter
    @Builder
    public static class Response {
        private LocationDto.Response location;
        private String nickname;
        private String email;
        private Member.Type type;

        public static Response of(Member member) {
            return Response.builder()
                    .nickname(member.getNickname())
                    .email(member.getEmail())
                    .location(LocationDto.Response.of(member.getLocation()))
                    .type(member.getType())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private String email;
        private String password;

        public Member toMember(PasswordEncoder passwordEncoder) {
            return Member.builder()
                    .email(getEmail())
                    .password(passwordEncoder.encode(password))
                    .authority(Member.Authority.ROLE_MEMBER)
                    .build();
        }

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(email, password);
        }
    }
}
