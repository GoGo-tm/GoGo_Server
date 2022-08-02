package com.tm.gogo.domain.member;

import com.tm.gogo.web.member.MemberResponse;
import com.tm.gogo.web.response.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tm.gogo.web.response.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberResponse findMemberByEmail(String email){
        return memberRepository.findByEmail(email)
                .map(MemberResponse::of)
                .orElseThrow(() -> new ApiException(MEMBER_NOT_FOUND, "사용자 정보가 없습니다. email: " + email));
    }

    public MemberResponse findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .map(MemberResponse::of)
                .orElseThrow(() -> new ApiException(MEMBER_NOT_FOUND, "사용자 정보가 없습니다. memberId: " + memberId));
    }

}
