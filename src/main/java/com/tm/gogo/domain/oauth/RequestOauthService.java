package com.tm.gogo.domain.oauth;

import com.tm.gogo.domain.member.Member;
import com.tm.gogo.web.oauth.OauthLoginRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RequestOauthService {
    private final Map<Member.Type, OauthApiClient> clients;

    public RequestOauthService(List<OauthApiClient> clients) {
        this.clients = clients.stream()
                .collect(Collectors.toUnmodifiableMap(OauthApiClient::getMemberType, Function.identity()));
    }

    public OauthInfo request(OauthLoginRequest oauthLoginRequest) {
        OauthApiClient oauthApiClient = clients.get(oauthLoginRequest.memberType());
        String accessToken = oauthApiClient.getOauthAccessToken(oauthLoginRequest);
        OauthProfileResponse oauthProfile = oauthApiClient.getOauthProfile(accessToken);

        return OauthInfo.builder()
                .email(oauthProfile.getEmail())
                .nickname(oauthProfile.getNickName())
                .type(oauthLoginRequest.memberType())
                .authority(Member.Authority.ROLE_MEMBER)
                .build();
    }
}
