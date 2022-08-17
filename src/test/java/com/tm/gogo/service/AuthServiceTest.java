package com.tm.gogo.service;

import com.tm.gogo.domain.auth.AuthService;
import com.tm.gogo.web.auth.*;
import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.jwt.TokenProvider;
import com.tm.gogo.domain.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 테스트")
    void testSignUp(){
        //given
        String nickname = "asdf";

        SignUpRequest signUpDto = SignUpRequest.builder()
                .nickname(nickname)
                .email("asdf@gmail.com")
                .password("12341234")
                .type(Member.Type.NATIVE)
                .build();

        //when
        SignUpResponse signupResponse = authService.signUp(signUpDto);

        //then : 실제로 내가 받은 email 로 저장된 데이터가 있는지 확인
        String email = signupResponse.getEmail();

        Member member = memberRepository.findByEmail(email).get();
        assertThat(member.getNickname()).isEqualTo(nickname);
        assertThat(member.getType()).isEqualTo(Member.Type.NATIVE);
    }

    @Test
    @DisplayName("로그인 테스트")
    void testSignIn(){
        //given
        String nickname = "asdf";
        String email = "asdf@gmail.com";

        SignUpRequest signUpDto = SignUpRequest.builder()
                .nickname(nickname)
                .email(email)
                .password("12341234")
                .type(Member.Type.NATIVE)
                .build();

        Member signUpMember = signUpDto.toMember(passwordEncoder);
        memberRepository.saveAndFlush(signUpMember);

        SignInRequest signInDto = SignInRequest.builder()
                .email(email)
                .password("12341234")
                .build();

        //when
        TokenResponse signInResponse = authService.signIn(signInDto);

        //then
        assertThat(signInResponse.getAccessToken()).isNotNull();
        assertThat(signInResponse.getRefreshToken()).isNotNull();
        assertThat(signInResponse.getGrantType()).isNotNull();
        assertThat(signInResponse.getAccessTokenExpiresIn()).isNotNull();

        String accessToken = signInResponse.getAccessToken();

        assertThat(tokenProvider.validateToken(accessToken)).isTrue();

        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        long memberId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByEmail(email).get();
        assertThat(member.getId()).isEqualTo(memberId);
    }
}
