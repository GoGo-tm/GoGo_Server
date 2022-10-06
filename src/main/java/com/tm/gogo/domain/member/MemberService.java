package com.tm.gogo.domain.member;


import com.tm.gogo.domain.token.NewPasswordTokenService;
import com.tm.gogo.domain.token.Token;
import com.tm.gogo.helper.MailService;
import com.tm.gogo.helper.RandomPasswordGenerator;
import com.tm.gogo.web.auth.UpdateTokenDto;
import com.tm.gogo.web.member.MemberRequest;
import com.tm.gogo.web.member.MemberResponse;
import com.tm.gogo.web.response.ApiException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tm.gogo.web.response.ErrorCode.MEMBER_NOT_FOUND;
import static com.tm.gogo.web.response.ErrorCode.PASSWORD_NOT_MATCH;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final NewPasswordTokenService newPasswordTokenService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    public MemberResponse findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .map(MemberResponse::of)
                .orElseThrow(() -> new ApiException(MEMBER_NOT_FOUND, "사용자 정보가 없습니다. memberId: " + memberId));
    }

    @Transactional
    public UpdateTokenDto issueToken(String email) {
        Token token = newPasswordTokenService.issueToken(RandomStringUtils.randomAlphanumeric(10), email);
        return UpdateTokenDto.of(token);
    }

    @Transactional
    public void updatePasswordAndSendMail(UpdateTokenDto updateTokenDto) {
        String txId = updateTokenDto.getTxId();
        String email = updateTokenDto.getEmail();

        newPasswordTokenService.validateToken(txId, email);

        String newPassword = RandomPasswordGenerator.generate();
        updatePassword(email, newPassword);
        mailService.sendNewPassword(email, newPassword);
    }

    @Transactional
    public void updatePassword(String email, String newPassword) {
        Member member = findByEmail(email);
        String encodedPassword = encode(newPassword);
        member.updatePassword(encodedPassword);
    }

    private Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(MEMBER_NOT_FOUND, "사용자 정보가 없습니다. email: " + email));
    }

    private Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(MEMBER_NOT_FOUND, "사용자 정보가 없습니다. memberId: " + memberId));
    }

    private String encode(String password) {
        return passwordEncoder.encode(password);
    }

    @Transactional
    public void update(Long memberId, MemberRequest memberRequest) {
        Member member = findById(memberId);

        boolean matches = passwordEncoder.matches(memberRequest.getPassword(), member.getPassword());
        if (!matches)
            throw new ApiException(PASSWORD_NOT_MATCH, "password 값이 다릅니다. password: " + memberRequest.getPassword());

        String encodedNewPassword = encode(memberRequest.getNewPassword());
        member.update(memberRequest, encodedNewPassword);
    }
}
