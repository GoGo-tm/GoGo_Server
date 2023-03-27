package com.tm.gogo.service;

import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.member.MemberRepository;
import com.tm.gogo.domain.member.QueryMemberService;
import com.tm.gogo.web.member.MemberResponse;
import com.tm.gogo.web.response.ApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
public class QueryMemberServiceTest {

    @Autowired
    private QueryMemberService queryMemberService;

    @Autowired
    private MemberRepository memberRepository;


    @Test
    @DisplayName("Id 로 회원 조회 성공")
    void testFindById() {
        // given
        String email = "woody@naver.net";
        String nickname = "woody";
        Member.Type type = Member.Type.NATIVE;

        Member member = Member.builder()
                .nickname(nickname)
                .email(email)
                .authority(Member.Authority.ROLE_MEMBER)
                .type(type)
                .build();

        memberRepository.saveAndFlush(member);

        // when
        MemberResponse memberResponse = queryMemberService.findMemberInfoById(member.getId());

        // then
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getNickname()).isEqualTo(nickname);
        assertThat(memberResponse.getType()).isEqualTo(type);
    }

    @Test
    @DisplayName("해당하는 memberId 회원이 없어서 실패")
    void testFailWhenNoMemberId() {
        Long memberId = 0L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> queryMemberService.findMemberInfoById(memberId))
                .withMessage("사용자 정보가 없습니다. memberId: " + memberId);
    }
}
