package com.tm.gogo.web.oauth;

import com.tm.gogo.domain.oauth.OauthService;
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
    private final OauthService oauthService;

    @PostMapping("/kakao")
    public ResponseEntity<TokenResponse> kakaoLogin(@RequestBody KakaoLoginRequest kakaoLoginRequest) {
        return ResponseEntity.ok(oauthService.login(kakaoLoginRequest));
    }

    @PostMapping("/naver")
    public ResponseEntity<TokenResponse> naverLogin(@RequestBody NaverLoginRequest naverLoginRequest) {
        return ResponseEntity.ok(oauthService.login(naverLoginRequest));
    }

    @PostMapping("/google")
    public ResponseEntity<TokenResponse> googleLogin(@RequestBody GoogleLoginRequest googleLoginRequest) {
        return ResponseEntity.ok(oauthService.login(googleLoginRequest));
    }
}
