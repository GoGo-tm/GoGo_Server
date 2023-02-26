package com.tm.gogo.service;

import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.oauth.OauthApiClient;
import com.tm.gogo.domain.oauth.OauthInfo;
import com.tm.gogo.domain.oauth.OauthProfileResponse;
import com.tm.gogo.domain.oauth.kakao.KakaoOauthService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class KaKaoOauthServiceTest {
    @Test
    void testKakaoOauth() {
        // given
        KakaoOauthService kakaoOauthService = new KakaoOauthService(new KakaoApiMockClient());

        String grantType = "";
        String clientId = "";
        String authorizationCode = "";

        // when
        OauthInfo kakaoInfo = kakaoOauthService.getKakaoInfo(grantType, clientId, authorizationCode);

        // then
        assertThat(kakaoInfo.getEmail()).isEqualTo("test@test.com");
        assertThat(kakaoInfo.getNickname()).isEqualTo("testNickname");
        assertThat(kakaoInfo.getType()).isEqualTo(Member.Type.KAKAO);
        assertThat(kakaoInfo.getAuthority()).isEqualTo(Member.Authority.ROLE_MEMBER);
    }

    static class KakaoApiMockClient implements OauthApiClient {

        @Override
        public String getOauthAccessToken(String grantType, String clientId, String authorizationCode) {
            return "accessToken";
        }

        @Override
        public OauthProfileResponse getOauthProfile(String accessToken) {
            return new KakaoMockInfo();
        }
    }

    static class KakaoMockInfo implements OauthProfileResponse {

        @Override
        public String getEmail() {
            return "test@test.com";
        }

        @Override
        public String getNickName() {
            return "testNickname";
        }
    }
}
