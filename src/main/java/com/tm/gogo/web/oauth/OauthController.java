package com.tm.gogo.web.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tm.gogo.domain.oauth.OauthInfo;
import com.tm.gogo.domain.oauth.OauthMemberService;
import com.tm.gogo.domain.oauth.kakao.KakaoOauthService;
import com.tm.gogo.web.auth.TokenResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "/oauth", description = "Oauth 관련 API")
@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class OauthController {

    private final KakaoOauthService kakaoOauthService;
    private final OauthMemberService oauthMemberService;

    @PostMapping("/kakao")
    public ResponseEntity<TokenResponse> kakaoLogin(@RequestBody KakaoLoginRequest kakaoLoginRequest) throws JsonProcessingException {
        OauthInfo kakaoInfo = kakaoOauthService.getKakaoInfo(kakaoLoginRequest.getGrantType(),
                                                             kakaoLoginRequest.getClientId(),
                                                             kakaoLoginRequest.getAuthorizationCode());

        TokenResponse tokenResponse = oauthMemberService.getAccessTokenWithOauthInfo(kakaoInfo);

        return ResponseEntity.ok(tokenResponse);
    }
}
