package com.tm.gogo.domain.oauth;

import com.tm.gogo.domain.jwt.TokenProvider;
import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.member.QueryMemberService;
import com.tm.gogo.domain.token.RefreshTokenService;
import com.tm.gogo.web.auth.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;


@Service
@Transactional
@RequiredArgsConstructor
public class OauthMemberService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final QueryMemberService queryMemberService;

    public TokenResponse getAccessTokenWithOauthInfo(OauthInfo oauthInfo) {
        Member member = queryMemberService.findOrCreateMember(oauthInfo);
        return generateToken(String.valueOf(member.getId()), getAuthorities(member));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Member member) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());
        return Collections.singleton(grantedAuthority);
    }

    private TokenResponse generateToken(String subject, Collection<? extends GrantedAuthority> authorities) {
        TokenResponse tokenDto = tokenProvider.generateTokenDto(subject, authorities);
        refreshTokenService.issueToken(subject, tokenDto.getRefreshToken());
        return tokenDto;
    }
}
