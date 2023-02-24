package com.tm.gogo.domain.oauth;

import com.tm.gogo.domain.jwt.TokenProvider;
import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.member.MemberRepository;
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
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    public TokenResponse getAccessTokenWithOauthInfo(OauthInfo oauthInfo) {
        Member member = findOrCreateMember(oauthInfo);
        return generateToken(String.valueOf(member.getId()), getAuthorities(member));
    }

    private Member findOrCreateMember(OauthInfo oauthInfo) {
        return memberRepository.findByEmail(oauthInfo.getEmail())
                               .orElseGet(() -> newMember(oauthInfo));
    }

    private Member newMember(OauthInfo oauthInfo) {
        Member member = Member.builder()
                .nickname(oauthInfo.getNickname())
                .email(oauthInfo.getEmail())
                .type(oauthInfo.getType())
                .authority(oauthInfo.getAuthority())
                .build();

        return memberRepository.save(member);
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
