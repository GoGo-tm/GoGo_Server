package com.tm.gogo.domain.member;


import com.tm.gogo.domain.token.Token;
import com.tm.gogo.domain.token.TokenRepository;
import com.tm.gogo.helper.MailService;
import com.tm.gogo.helper.RandomPasswordGenerator;
import com.tm.gogo.web.auth.UpdateTokenResponse;
import com.tm.gogo.web.member.MemberResponse;
import com.tm.gogo.web.response.ApiException;
import com.tm.gogo.web.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.tm.gogo.web.response.ErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    private final TokenRepository tokenRepository;
    
    public MemberResponse findMemberByEmail(String email) {
        Member member = findByEmail(email);
        return MemberResponse.of(member);
    }

    public MemberResponse findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .map(MemberResponse::of)
                .orElseThrow(() -> new ApiException(MEMBER_NOT_FOUND, "사용자 정보가 없습니다. memberId: " + memberId));
    }


    @Transactional
    public void updatePasswordAndSendMail(UpdateTokenResponse updateTokenResponse) {
        String email = validationToken(updateTokenResponse);
        String newPassword = RandomPasswordGenerator.generate();
        updatePassword(email, newPassword);
        mailService.sendNewPassword(email, newPassword);
    }
    public UpdateTokenResponse issueToken(String email){
        Token issueToken = Token.builder()
                .key(RandomStringUtils.randomAlphanumeric(10))//랜덤 키 값
                .value(email)
                .expiredAt(LocalDateTime.now().plusSeconds(10))
                .type(Token.Type.ISSUE)
                .build();

        tokenRepository.save(issueToken);

        return UpdateTokenResponse.of(issueToken);
    }

    public String validationToken(UpdateTokenResponse updateTokenResponse) {
        String txId = updateTokenResponse.getTxId();
        Token issueToken = tokenRepository.findByKeyAndType(txId, Token.Type.ISSUE)
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST, "존재하지 않는 키 값입니다."));

        if(!issueToken.getExpiredAt().isAfter(LocalDateTime.now())) {
            throw new ApiException(ErrorCode.EXPIRED_TOKEN, "토큰 만료 기간이 지났습니다.");
        }

        return issueToken.getValue();
    }

    @Transactional
    public void updatePassword(String email, String newPassword) {
        Member member = findByEmail(email);
        String encodedPassword = passwordEncoder.encode(newPassword);
        member.updatePassword(encodedPassword);
    }

    private Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(MEMBER_NOT_FOUND, "사용자 정보가 없습니다. email: " + email));
    }
}

