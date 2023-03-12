package com.tm.gogo.web.oauth;

import com.tm.gogo.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@NoArgsConstructor
public class NaverLoginRequest implements OauthLoginRequest {

    @Schema(example = "authorization_code")
    private String grantType;

    @Schema(example = "xt7snAboggM8W0GxDvbM")
    private String clientId;

    @Schema(example = "jYHm2t6tggTCSsb50x")
    private String authorizationCode;

    @Schema(example = "hLiDdL2uhPtsftcU")
    private String state;

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add("grant_type", grantType);
        body.add("client_id", clientId);
        body.add("code", authorizationCode);
        body.add("state", state);

        return body;
    }

    @Override
    public Member.Type memberType() {
        return Member.Type.NAVER;
    }
}
