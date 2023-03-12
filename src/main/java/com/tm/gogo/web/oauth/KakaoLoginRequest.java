package com.tm.gogo.web.oauth;

import com.tm.gogo.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@NoArgsConstructor
public class KakaoLoginRequest implements OauthLoginRequest {

    @Schema(example = "authorization_code")
    private String grantType;

    @Schema(example = "325b2ae4fab6a8fcafd9290b655434f2")
    private String clientId;

    @Schema(example = "1Ee8i2_T1j4Y0D7BXL7EDx6iLzLwes6V7Ans1wEFf_icqrFsssJ4wV1dXOEbLYBqSJfPGAo9dZwAAAGGgy4p6g")
    private String authorizationCode;

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add("grant_type", grantType);
        body.add("client_id", clientId);
        body.add("code", authorizationCode);

        return body;
    }

    @Override
    public Member.Type memberType() {
        return Member.Type.KAKAO;
    }
}
