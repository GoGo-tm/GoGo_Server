package com.tm.gogo.web.auth;

import com.tm.gogo.domain.member.Member;
import com.tm.gogo.web.member.LocationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;


public class SignUpDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request{
        private String nickname;
        private String email;
        private String password;
        private LocationDto location;
        private Member.Type type;

        public Member toMember(PasswordEncoder passwordEncoder) {
            return Member.builder()
                    .nickname(nickname)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .type(type)
                    .location(location.toLocation())
                    .authority(Member.Authority.ROLE_MEMBER)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String email;

        public static Response of(Member member) {
            return new Response(member.getEmail());
        }
    }
}
