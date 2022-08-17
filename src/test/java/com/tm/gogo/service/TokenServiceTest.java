package com.tm.gogo.service;

import com.tm.gogo.domain.Location;
import com.tm.gogo.domain.auth.AuthService;
import com.tm.gogo.domain.jwt.TokenProvider;
import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.member.MemberRepository;
import com.tm.gogo.domain.token.NewPasswordTokenService;
import com.tm.gogo.domain.token.RefreshTokenService;
import com.tm.gogo.domain.token.Token;
import com.tm.gogo.domain.token.TokenRepository;
import com.tm.gogo.helper.RandomPasswordGenerator;
import com.tm.gogo.web.auth.*;
import com.tm.gogo.web.member.LocationDto;
import com.tm.gogo.web.response.ApiException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
public class TokenServiceTest {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private NewPasswordTokenService newPasswordTokenService;
    @Autowired
    private AuthService authService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private TokenProvider tokenProvider;

    @Nested
    @DisplayName("새로운 비밀번호 토큰 테스트")
    class newPasswordTest {
        @Test
        @DisplayName("비밀번호 변경 성공")
        void testUpdatePassword() {
            // given
            String email = "dustle@naver.net";
            String nickname = "dustle";
            Member.Type type = Member.Type.NATIVE;

            String locationName = "서울";
            float latitude = 1F;
            float longitude = 2F;

            Location location = Location.builder()
                    .name(locationName)
                    .latitude(latitude)
                    .longitude(longitude)
                    .build();

            Member member = Member.builder()
                    .nickname(nickname)
                    .email(email)
                    .password("12341234")
                    .authority(Member.Authority.ROLE_MEMBER)
                    .type(type)
                    .location(location)
                    .build();

            memberRepository.saveAndFlush(member);

            // when
            String newPassword = RandomPasswordGenerator.generate();
            newPasswordTokenService.updatePassword(member.getEmail(), newPassword);

            // then
            Member updatedMember = memberRepository.findByEmail(email).get();
            boolean matches = passwordEncoder.matches(newPassword, updatedMember.getPassword());
            assertThat(matches).isTrue();
        }
        @Test
        @DisplayName("비밀번호 변경 토큰 검증 성공")
        void testValidationToken() {
            //given
            String email = "dustle@gmail.com";
            Token issueToken = Token.builder()
                    .key(RandomStringUtils.randomAlphanumeric(10))//랜덤 키 값
                    .value(email)
                    .expiredAt(LocalDateTime.now().plusSeconds(10))
                    .type(Token.Type.NEW_PASSWORD)
                    .build();

            tokenRepository.saveAndFlush(issueToken);
            UpdateTokenDto tokenDto = UpdateTokenDto.of(issueToken);

            //when
            String tokenEmail = newPasswordTokenService.validationToken(tokenDto.getTxId(), tokenDto.getType());
            newPasswordTokenService.validationEmail(tokenDto.getEmail(), email);


            //then
            assertThat(tokenEmail).isEqualTo(email);
        }
        @Test
        @DisplayName("비밀번호 변경 토큰 만료 기간 지나서 검증 실패")
        void testFailValidationToken() {
            //given
            String email = "dustle@gmail.com";
            Token issueToken = Token.builder()
                    .key(RandomStringUtils.randomAlphanumeric(10))//랜덤 키 값
                    .value(email)
                    .expiredAt(LocalDateTime.now().minusMinutes(10))
                    .type(Token.Type.NEW_PASSWORD)
                    .build();

            tokenRepository.saveAndFlush(issueToken);
            UpdateTokenDto tokenDto = UpdateTokenDto.of(issueToken);

            //then
            assertThatExceptionOfType(ApiException.class)
                    .isThrownBy(() -> newPasswordTokenService.validationToken(tokenDto.getTxId(), tokenDto.getType()))
                    .withMessage("토큰 만료 기간이 지났습니다. txId: " + tokenDto.getTxId());
        }
    }

    @Nested
    @DisplayName("토큰 재발급 테스트")
    class refreshTokenTest {
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

            TokenResponse tokenResponseDto = authService.signIn(signInDto);

            TokenRequest tokenDto = TokenRequest.builder()
                    .accessToken(tokenResponseDto.getAccessToken())
                    .refreshToken(tokenResponseDto.getRefreshToken())
                    .build();

            //when
            TokenResponse reissueToken = refreshTokenService.reissue(tokenDto);

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
}
