package com.tm.gogo.domain.auth;

import com.tm.gogo.domain.token.Token;
import com.tm.gogo.domain.token.TokenRepository;
import com.tm.gogo.web.auth.*;
import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.jwt.TokenProvider;
import com.tm.gogo.domain.member.MemberRepository;
import com.tm.gogo.web.response.ApiException;
import com.tm.gogo.web.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.tm.gogo.web.response.ErrorCode.ALREADY_EXIST_MEMBER;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public SignUpResponse signUp(SignUpRequest signUpDto) {
        if (memberRepository.existsByEmail(signUpDto.getEmail())) {
            throw new ApiException(ALREADY_EXIST_MEMBER, "이미 가입되어 있는 유저입니다. email: " + signUpDto.getEmail());
        }

        Member member = signUpDto.toMember(passwordEncoder);
        memberRepository.save(member);
        return SignUpResponse.of(member);
    }

    @Transactional
    public TokenResponse signIn(SignInRequest signInDto) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = signInDto.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenResponse tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        Token refreshToken = Token.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .expiredAt(LocalDateTime.now().plusWeeks(2))
                .type(Token.Type.REFRESH)
                .build();

        tokenRepository.save(refreshToken);

        // 5. 토큰 발급
        return tokenDto;
    }

    @Transactional
    public TokenResponse reissue(TokenRequest tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new ApiException(ErrorCode.INVALID_REFRESH_TOKEN, "Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        Token refreshToken = tokenRepository.findByKeyAndType(authentication.getName(), Token.Type.REFRESH)
                .orElseThrow(() -> new ApiException(ErrorCode.UNAUTHORIZED_REFRESH_TOKEN, "로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (refreshToken.isNotEqualTo(tokenRequestDto.getRefreshToken())) {
            throw new ApiException(ErrorCode.REFRESH_TOKEN_NOT_MATCH, "토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenResponse tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        refreshToken.renewValue(tokenDto.getRefreshToken(), LocalDateTime.now().plusWeeks(2));

        // 토큰 발급
        return tokenDto;
    }
}

