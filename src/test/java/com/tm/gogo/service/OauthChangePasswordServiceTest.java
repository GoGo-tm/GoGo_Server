package com.tm.gogo.service;

import com.tm.gogo.domain.jwt.TokenProvider;
import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.member.MemberRepository;
import com.tm.gogo.domain.oauth.OauthInfo;
import com.tm.gogo.domain.oauth.OauthMemberService;
import com.tm.gogo.web.auth.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class OauthChangePasswordServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private OauthMemberService oauthMemberService;

    @Test
    @DisplayName("카카오로 로그인 테스트 - 회원 가입이 되어 있지 않으면 가입 처리")
    void testKakaoSignup() {
        // given
        String email = "test@kakao.com";
        String nickname = "testRyan";
        OauthInfo oauthInfo = OauthInfo.builder()
                                       .email(email)
                                       .nickname(nickname)
                                       .type(Member.Type.KAKAO)
                                       .authority(Member.Authority.ROLE_MEMBER)
                                       .build();

        // when
        TokenResponse tokenResponse = oauthMemberService.getAccessTokenWithOauthInfo(oauthInfo);

        // then
        assertThat(tokenResponse.getAccessToken()).isNotNull();
        assertThat(tokenResponse.getRefreshToken()).isNotNull();
        assertThat(tokenResponse.getGrantType()).isNotNull();
        assertThat(tokenResponse.getAccessTokenExpiresIn()).isNotNull();

        String accessToken = tokenResponse.getAccessToken();

        assertThat(tokenProvider.validateToken(accessToken)).isTrue();

        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        long memberId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByEmail(email).get();
        assertThat(member.getId()).isEqualTo(memberId);
        assertThat(member.getNickname()).isEqualTo(nickname);
    }
}
