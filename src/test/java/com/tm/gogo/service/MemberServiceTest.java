package com.tm.gogo.service;

import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.member.MemberRepository;
import com.tm.gogo.domain.member.MemberService;
import com.tm.gogo.domain.token.Token;
import com.tm.gogo.domain.token.TokenRepository;
import com.tm.gogo.helper.RandomPasswordGenerator;
import com.tm.gogo.web.member.MemberResponse;
import com.tm.gogo.web.response.ApiException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenRepository tokenRepository;

    @Nested
    @DisplayName("회원 조회")
    class FindTest {

        @Test
        @DisplayName("이메일로 회원 조회 성공")
        void testFindByEmail() {
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
            MemberResponse memberResponse = memberService.findMemberByEmail(email);

            // then
            assertThat(memberResponse.getEmail()).isEqualTo(email);
            assertThat(memberResponse.getNickname()).isEqualTo(nickname);
            assertThat(memberResponse.getType()).isEqualTo(type);
        }

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
            MemberResponse memberResponse = memberService.findMemberById(member.getId());

            // then
            assertThat(memberResponse.getEmail()).isEqualTo(email);
            assertThat(memberResponse.getNickname()).isEqualTo(nickname);
            assertThat(memberResponse.getType()).isEqualTo(type);
        }

        @Test
        @DisplayName("해당하는 Email 회원이 없어서 실패")
        void testFailWhenNoEmail() {
            String email = "no-member@naver.net";

            assertThatExceptionOfType(ApiException.class)
                    .isThrownBy(() -> memberService.findMemberByEmail(email))
                    .withMessage("사용자 정보가 없습니다. email: " + email);
        }

        @Test
        @DisplayName("해당하는 memberId 회원이 없어서 실패")
        void testFailWhenNoMemberId() {
            Long memberId = 0L;

            assertThatExceptionOfType(ApiException.class)
                    .isThrownBy(() -> memberService.findMemberById(memberId))
                    .withMessage("사용자 정보가 없습니다. memberId: " + memberId);
        }
    }

    @Test
    @DisplayName("비밀번호 변경 성공")
    void testUpdatePassword() {
        // given
        String email = "dustle@naver.net";
        String nickname = "dustle";
        Member.Type type = Member.Type.NATIVE;

        String locationName = "서울";
        float latitude = 1F;
        float longitude = 2F;

        Location location = Location.builder()
                .name(locationName)
                .latitude(latitude)
                .longitude(longitude)
                .build();

        Member member = Member.builder()
                .nickname(nickname)
                .email(email)
                .password("12341234")
                .authority(Member.Authority.ROLE_MEMBER)
                .type(type)
                .location(location)
                .build();

        memberRepository.saveAndFlush(member);

        // when
        String newPassword = RandomPasswordGenerator.generate();
        memberService.updatePassword(member.getEmail(), newPassword);

        // then
        Member updatedMember = memberRepository.findByEmail(email).get();
        boolean matches = passwordEncoder.matches(newPassword, updatedMember.getPassword());
        assertThat(matches).isTrue();
    }
    @Test
    @DisplayName("비밀번호 변경 토큰 검증 성공")
    void testValidationToken(){
        //given
        String email = "dustle@gmail.com";
        Token issueToken = Token.builder()
                .key(RandomStringUtils.randomAlphanumeric(10))//랜덤 키 값
                .value(email)
                .expiredAt(LocalDateTime.now().plusSeconds(10))
                .type(Token.Type.ISSUE)
                .build();

        tokenRepository.saveAndFlush(issueToken);
        UpdateTokenResponse tokenDto = UpdateTokenResponse.of(issueToken);

        //when
        String tokenEmail = memberService.validationToken(tokenDto);

        //then
        assertThat(tokenEmail).isEqualTo(email);
    }
    @Test
    @DisplayName("비밀번호 변경 토큰 만료 기간 지나서 검증 실패")
    void testFailValidationToken(){
        //given
        String email = "dustle@gmail.com";
        Token issueToken = Token.builder()
                .key(RandomStringUtils.randomAlphanumeric(10))//랜덤 키 값
                .value(email)
                .expiredAt(LocalDateTime.now().minusMinutes(10))
                .type(Token.Type.ISSUE)
                .build();

        tokenRepository.saveAndFlush(issueToken);
        UpdateTokenResponse tokenDto = UpdateTokenResponse.of(issueToken);

        //then
        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> memberService.validationToken(tokenDto))
                .withMessage("토큰 만료 기간이 지났습니다.");
    }

}
