package com.tm.gogo.domain.term_agreement;

import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.member.MemberRepository;
import com.tm.gogo.web.response.ApiException;
import com.tm.gogo.web.term_agreement.TermAgreementResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.tm.gogo.web.response.ErrorCode.MEMBER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TermAgreementService {

    private final MemberRepository memberRepository;

    public List<TermAgreementResponse> findTermAgreements(Long memberId) {
        return findMember(memberId).getTermAgreements().stream()
                .map(TermAgreementResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateTermAgreement(Long memberId, Term term, Boolean agreed) {
        findMember(memberId).updateTermAgreed(term, agreed);
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(MEMBER_NOT_FOUND, "사용자 정보가 없습니다. memberId: " + memberId));
    }
}
