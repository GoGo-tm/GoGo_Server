package com.tm.gogo.domain.member;

import com.tm.gogo.domain.oauth.OauthInfo;
import com.tm.gogo.web.member.MemberResponse;
import com.tm.gogo.web.response.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tm.gogo.web.response.ErrorCode.MEMBER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QueryMemberService {

    private final MemberRepository memberRepository;

    public MemberResponse findMemberInfoById(Long memberId) {
        return memberRepository.findById(memberId)
                .map(MemberResponse::of)
                .orElseThrow(() -> new ApiException(MEMBER_NOT_FOUND, "사용자 정보가 없습니다. memberId: " + memberId));
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(MEMBER_NOT_FOUND, "사용자 정보가 없습니다. email: " + email));
    }

    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(MEMBER_NOT_FOUND, "사용자 정보가 없습니다. memberId: " + memberId));
    }

    public Member findOrCreateMember(OauthInfo oauthInfo) {
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
}
