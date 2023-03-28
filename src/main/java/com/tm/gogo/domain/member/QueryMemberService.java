package com.tm.gogo.domain.member;

import com.tm.gogo.web.member.MemberResponse;
import com.tm.gogo.web.response.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.tm.gogo.web.response.ErrorCode.MEMBER_NOT_FOUND;

@Service
@CacheConfig(cacheNames = "Member")
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QueryMemberService {

    private final MemberRepository memberRepository;

    @Cacheable
    public MemberResponse findMemberInfoById(Long memberId) {
        return memberRepository.findById(memberId)
                .map(MemberResponse::of)
                .orElseThrow(() -> new ApiException(MEMBER_NOT_FOUND, "사용자 정보가 없습니다. memberId: " + memberId));
    }

    @Cacheable
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(MEMBER_NOT_FOUND, "사용자 정보가 없습니다. email: " + email));
    }

    @Cacheable
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(MEMBER_NOT_FOUND, "사용자 정보가 없습니다. memberId: " + memberId));
    }

    @Cacheable
    public Optional<Member> findOptionalByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public boolean existsEmail(String email) {
        return memberRepository.existsByEmail(email);
    }
}
