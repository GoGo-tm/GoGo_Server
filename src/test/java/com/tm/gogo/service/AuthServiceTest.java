package com.tm.gogo.service;

import com.tm.gogo.domain.auth.AuthService;
import com.tm.gogo.web.auth.SignUpResponse;
import com.tm.gogo.web.member.LocationDto;
import com.tm.gogo.web.auth.SignInRequest;
import com.tm.gogo.web.auth.SignUpRequest;
import com.tm.gogo.web.auth.TokenDto;
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
        String locationName = "바퀴산";
        float latitude = 1234123123F;
        float longitude = 1234123123F;

        LocationDto locationDto = LocationDto.builder()
                .name(locationName)
                .latitude(latitude)
                .longitude(longitude)
                .build();

        SignUpRequest signUpDto = SignUpRequest.builder()
                .nickname(nickname)
                .email("asdf@gmail.com")
                .password("12341234")
                .type(Member.Type.NATIVE)
                .location(locationDto)
                .build();

        //when
        SignUpResponse signupResponse = authService.signUp(signUpDto);

        //then : 실제로 내가 받은 email 로 저장된 데이터가 있는지 확인
        String email = signupResponse.getEmail();

        Member member = memberRepository.findByEmail(email).get();
        assertThat(member.getNickname()).isEqualTo(nickname);
        assertThat(member.getType()).isEqualTo(Member.Type.NATIVE);
        assertThat(member.getLocation().getName()).isEqualTo(locationName);
        assertThat(member.getLocation().getLatitude()).isEqualTo(latitude);
        assertThat(member.getLocation().getLongitude()).isEqualTo(longitude);
    }

    @Test
    @DisplayName("로그인 테스트")
    void testSignIn(){
        //given
        String locationName = "바퀴산";
        String nickname = "asdf";
        String email = "asdf@gmail.com";
        float latitude = 1234123123F;
        float longitude = 1234123123F;

        LocationDto locationDto = LocationDto.builder()
                .name(locationName)
                .latitude(latitude)
                .longitude(longitude)
                .build();

        SignUpRequest signUpDto = SignUpRequest.builder()
                .nickname(nickname)
                .email(email)
                .password("12341234")
                .type(Member.Type.NATIVE)
                .location(locationDto)
                .build();

        Member signUpMember = signUpDto.toMember(passwordEncoder);
        memberRepository.saveAndFlush(signUpMember);

        SignInRequest signInDto = SignInRequest.builder()
                .email(email)
                .password("12341234")
                .build();

        //when
        TokenDto.Response signInResponse = authService.signIn(signInDto);

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

    @Test
    @DisplayName("토큰 재발급 테스트")
    void testReissue() {
        //given
        String locationName = "바퀴산";
        String nickname = "asdf";
        String email = "asdf@gmail.com";
        String password = "12341234";
        float latitude = 1234123123F;
        float longitude = 1234123123F;

        LocationDto locationDto = LocationDto.builder()
                .name(locationName)
                .latitude(latitude)
                .longitude(longitude)
                .build();

        SignUpRequest signUpDto = SignUpRequest.builder()
                .nickname(nickname)
                .email(email)
                .password(password)
                .type(Member.Type.NATIVE)
                .location(locationDto)
                .build();

        authService.signUp(signUpDto);

        SignInRequest signInDto = SignInRequest.builder()
                .email(email)
                .password(password)
                .build();

        TokenDto.Response tokenResponseDto = authService.signIn(signInDto);

        TokenDto.Request tokenDto = TokenDto.Request.builder()
                .accessToken(tokenResponseDto.getAccessToken())
                .refreshToken(tokenResponseDto.getRefreshToken())
                .build();

        //when
        TokenDto.Response reissueToken = authService.reissue(tokenDto);

        //then
        assertThat(reissueToken.getAccessToken()).isNotNull();
        assertThat(reissueToken.getRefreshToken()).isNotNull();
        assertThat(reissueToken.getGrantType()).isNotNull();
        assertThat(reissueToken.getAccessTokenExpiresIn()).isNotNull();

        String accessToken = reissueToken.getAccessToken();

        assertThat(tokenProvider.validateToken(accessToken)).isTrue();

        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        long memberId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByEmail(email).get();
        assertThat(member.getId()).isEqualTo(memberId);
    }
}
