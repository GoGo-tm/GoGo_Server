package com.tm.gogo.domain.member;

import com.tm.gogo.web.member.MemberRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@CacheConfig(cacheNames = "Member")
@Transactional
@RequiredArgsConstructor
public class CommandMemberService {

    private final QueryMemberService queryMemberService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @CacheEvict(allEntries = true)
    public void updatePassword(String email, String newPassword) {
        Member member = queryMemberService.findByEmail(email);
        String encodedPassword = passwordEncoder.encode(newPassword);
        member.updatePassword(encodedPassword);
    }

    @CacheEvict(allEntries = true)
    public void updateMember(Member member, MemberRequest memberRequest, String encodedNewPassword) {
        member.update(memberRequest.getNickname(), memberRequest.getEmail(), encodedNewPassword, memberRequest.isAgreed());
    }

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    @CacheEvict(allEntries = true)
    public void delete(Long memberId) {
        memberRepository.deleteById(memberId);
    }
}
