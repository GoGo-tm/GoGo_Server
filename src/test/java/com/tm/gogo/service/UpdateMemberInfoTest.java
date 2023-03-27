package com.tm.gogo.service;

import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.member.MemberRepository;
import com.tm.gogo.domain.member.UpdateMemberInfoService;
import com.tm.gogo.domain.term_agreement.Term;
import com.tm.gogo.web.member.MemberRequest;
import com.tm.gogo.web.response.ApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
public class UpdateMemberInfoTest {

    @Autowired
    private UpdateMemberInfoService updateMemberInfoService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("내 정보 수정 성공")
    void testUpdate() {
        // given
        String email = "dustle@naver.net";
        String nickname = "dustle";
        Member.Type type = Member.Type.NATIVE;
        String password = passwordEncoder.encode("12341234");

        Member member = Member.builder()
                .nickname(nickname)
                .email(email)
                .password(password)
                .authority(Member.Authority.ROLE_MEMBER)
                .type(type)
                .build();

        memberRepository.saveAndFlush(member);

        //when
        MemberRequest memberRequest = MemberRequest.builder()
                .nickname("호롱")
                .email("asdf@naver.net")
                .password("12341234")
                .newPassword("131313")
                .agreed(true)
                .build();

        updateMemberInfoService.updateMemberInfo(member.getId(), memberRequest);

        //then
        Member updatedMember = memberRepository.findById(member.getId()).get();
        boolean matches = passwordEncoder.matches("131313", updatedMember.getPassword());
        assertThat(matches).isTrue();

        assertThat(updatedMember.getNickname()).isEqualTo("호롱");

        member.getTermAgreements().forEach(agreement -> {
            if (agreement.equalTerm(Term.LOCATION)) {
                assertThat(agreement.getAgreed()).isTrue();
            }
        });
    }

    @Test
    @DisplayName("내 정보 수정 비밀번호를 틀려서 실패")
    void testUpdateFail() {
        // given
        String email = "dustle@naver.net";
        String nickname = "dustle";
        Member.Type type = Member.Type.NATIVE;
        String password = passwordEncoder.encode("12341234");

        Member member = Member.builder()
                .nickname(nickname)
                .email(email)
                .password(password)
                .authority(Member.Authority.ROLE_MEMBER)
                .type(type)
                .build();

        memberRepository.saveAndFlush(member);

        //when
        MemberRequest memberRequest = MemberRequest.builder()
                .nickname("호롱")
                .email("asdf@naver.net")
                .password("123")
                .newPassword("131313")
                .agreed(true)
                .build();

        //then
        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> updateMemberInfoService.updateMemberInfo(member.getId(), memberRequest))
                .withMessage("password 값이 다릅니다. password: " + memberRequest.getPassword());
    }
}
