package com.tm.gogo.web.auth;


import com.tm.gogo.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpResponse {
    private String email;

    public static SignUpResponse of(Member member) {
        return new SignUpResponse(member.getEmail());
    }
}
