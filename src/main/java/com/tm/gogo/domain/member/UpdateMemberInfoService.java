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
public class UpdateMemberInfoService {

    private final CommandMemberService commandMemberService;
    private final QueryMemberService queryMemberService;
    private final PasswordEncoder passwordEncoder;

    public void updateMemberInfo(Long memberId, MemberRequest memberRequest) {
        Member member = queryMemberService.findById(memberId);

        validatePassword(memberRequest, member);

        String encodedNewPassword = passwordEncoder.encode(memberRequest.getNewPassword());
        commandMemberService.updateMember(member, memberRequest, encodedNewPassword);
    }

    private void validatePassword(MemberRequest memberRequest, Member member) {
        boolean matches = passwordEncoder.matches(memberRequest.getPassword(), member.getPassword());

        if (!matches) {
            throw new ApiException(PASSWORD_NOT_MATCH, "password 값이 다릅니다. password: " + memberRequest.getPassword());
        }
    }
}
