package com.tm.gogo.service;

import com.tm.gogo.domain.member.CommandMemberService;
import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.member.MemberRepository;
import com.tm.gogo.helper.RandomPasswordGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ChangePasswordServiceTest {

    @Autowired
    private CommandMemberService commandMemberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("비밀번호 변경 성공")
    void testUpdatePassword() {
        // given
        String email = "dustle@naver.net";
        String nickname = "dustle";
        Member.Type type = Member.Type.NATIVE;

        Member member = Member.builder()
                .nickname(nickname)
                .email(email)
                .password("12341234")
                .authority(Member.Authority.ROLE_MEMBER)
                .type(type)
                .build();

        memberRepository.saveAndFlush(member);

        // when
        String newPassword = RandomPasswordGenerator.generate();
        commandMemberService.updatePassword(member.getEmail(), newPassword);

        // then
        Member updatedMember = memberRepository.findByEmail(email).get();
        boolean matches = passwordEncoder.matches(newPassword, updatedMember.getPassword());
        assertThat(matches).isTrue();
    }
}
