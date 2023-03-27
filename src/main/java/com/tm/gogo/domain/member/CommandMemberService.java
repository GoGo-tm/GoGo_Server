package com.tm.gogo.domain.member;

import com.tm.gogo.web.member.MemberRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommandMemberService {

    private final QueryMemberService queryMemberService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void updatePassword(String email, String newPassword) {
        Member member = queryMemberService.findByEmail(email);
        String encodedPassword = passwordEncoder.encode(newPassword);
        member.updatePassword(encodedPassword);
    }

    public void updateMember(Member member, MemberRequest memberRequest, String encodedNewPassword) {
        member.update(memberRequest.getNickname(), memberRequest.getEmail(), encodedNewPassword, memberRequest.isAgreed());
    }

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public void delete(Long memberId) {
        memberRepository.deleteById(memberId);
    }
}
