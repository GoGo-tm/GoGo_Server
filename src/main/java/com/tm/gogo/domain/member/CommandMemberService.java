package com.tm.gogo.domain.member;

import com.tm.gogo.web.member.MemberRequest;
import com.tm.gogo.web.response.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tm.gogo.web.response.ErrorCode.PASSWORD_NOT_MATCH;

@Service
@Transactional
@RequiredArgsConstructor
public class CommandMemberService {

    private final QueryMemberService queryMemberService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void updatePassword(String email, String newPassword) {
        Member member = queryMemberService.findByEmail(email);
        String encodedPassword = passwordEncoder.encode(newPassword);
        member.updatePassword(encodedPassword);
    }

    @Transactional
    public void update(Long memberId, MemberRequest memberRequest) {
        Member member = queryMemberService.findMemberById(memberId);

        boolean matches = passwordEncoder.matches(memberRequest.getPassword(), member.getPassword());

        if (!matches) {
            throw new ApiException(PASSWORD_NOT_MATCH, "password 값이 다릅니다. password: " + memberRequest.getPassword());
        }

        String encodedNewPassword = passwordEncoder.encode(memberRequest.getNewPassword());
        member.update(memberRequest.getNickname(), memberRequest.getEmail(), encodedNewPassword, memberRequest.isAgreed());
    }

    public void delete(Long memberId) {
        memberRepository.deleteById(memberId);
    }

}
