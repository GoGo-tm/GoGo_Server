package com.tm.gogo.domain.term_agreement;

import com.tm.gogo.domain.member.MemberService;
import com.tm.gogo.web.term_agreement.TermAgreementResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TermAgreementService {

    private final MemberService memberService;


    public List<TermAgreementResponse> findTermAgreements(Long memberId) {
        return memberService.findMemberById(memberId).getTermAgreements().stream()
                .map(TermAgreementResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateTermAgreement(Long memberId, Term term, Boolean agreed) {
        memberService.findMemberById(memberId).updateTermAgreed(term, agreed);
    }
}
