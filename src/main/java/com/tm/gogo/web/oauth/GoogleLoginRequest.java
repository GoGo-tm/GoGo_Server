package com.tm.gogo.web.oauth;

import com.tm.gogo.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@NoArgsConstructor
public class GoogleLoginRequest implements OauthLoginRequest {

    @Schema(example = "authorization_code")
    private String grantType;

    @Schema(example = "174745291416-js5bgkh03d5mr0or8inlhelikd63anoe.apps.googleusercontent.com")
    private String clientId;

    @Schema(example = "4/0AWtgzh7KFeygZRxtt9QmnGk3qb_p8y0ObTQdfwEiWdaiCnXDebV5h-ZAZ-Q7r0LCVWUAeg")
    private String authorizationCode;

    @Schema(example = "http://localhost:8080/google/callback")
    private String redirectUri;

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add("grant_type", grantType);
        body.add("client_id", clientId);
        body.add("code", authorizationCode);
        body.add("redirect_uri", redirectUri);

        return body;
    }

    @Override
    public Member.Type memberType() {
        return Member.Type.GOOGLE;
    }
}
