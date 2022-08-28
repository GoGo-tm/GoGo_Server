package com.tm.gogo.service;

import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.member.MemberRepository;
import com.tm.gogo.domain.term_agreement.Term;
import com.tm.gogo.domain.term_agreement.TermAgreementService;
import com.tm.gogo.web.term_agreement.TermAgreementResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class TermAgreementServiceTest {

    @Autowired
    private TermAgreementService termAgreementService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("Member 의 모든 약관 조회")
    void testFindTermAgreements() {
        // given
        EnumSet<Term> agreeTerms = EnumSet.of(Term.PRIVACY_POLICY, Term.SERVICE);
        Member member = Member.builder().terms(agreeTerms).build();
        memberRepository.saveAndFlush(member);

        // when
        List<TermAgreementResponse> responses = termAgreementService.findTermAgreements(member.getId());

        // then
        assertThat(responses.size()).isEqualTo(Term.values().length);
        for (TermAgreementResponse response : responses) {
            if (response.getAgreed()) {
                assertThat(agreeTerms).contains(response.getTerm());
            } else {
                assertThat(agreeTerms).doesNotContain(response.getTerm());
            }
        }
    }

    @Nested
    @DisplayName("약관 동의/철회")
    class AgreeTest {

        @Test
        @DisplayName("LOCATION 약관 동의 처리")
        void testAgree() {
            // given
            EnumSet<Term> agreeTerms = EnumSet.of(Term.PRIVACY_POLICY, Term.SERVICE);
            Member member = Member.builder().terms(agreeTerms).build();
            memberRepository.saveAndFlush(member);

            // when
            termAgreementService.updateTermAgreement(member.getId(), Term.LOCATION, true);

            // then
            member.getTermAgreements().forEach(agreement -> {
                if (agreement.equalTerm(Term.LOCATION)) {
                    assertThat(agreement.getAgreed()).isTrue();
                } else {
                    assertThat(agreement.getAgreed()).isTrue();
                }
            });
        }

        @Test
        @DisplayName("PRIVACY_POLICY 약관 철회")
        void testDisagree() {
            // given
            EnumSet<Term> agreeTerms = EnumSet.of(Term.PRIVACY_POLICY, Term.SERVICE);
            Member member = Member.builder().terms(agreeTerms).build();
            memberRepository.saveAndFlush(member);

            // when
            termAgreementService.updateTermAgreement(member.getId(), Term.PRIVACY_POLICY, false);

            // then
            member.getTermAgreements().forEach(agreement -> {
                if (agreement.equalTerm(Term.PRIVACY_POLICY)) {
                    assertThat(agreement.getAgreed()).isFalse();
                } else if (agreement.equalTerm(Term.SERVICE)) {
                    assertThat(agreement.getAgreed()).isTrue();
                } else {
                    assertThat(agreement.getAgreed()).isFalse();
                }
            });
        }
    }

}
