package com.tm.gogo.service;

import com.tm.gogo.domain.Location;
import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.member.MemberRepository;
import com.tm.gogo.domain.member.MemberService;
import com.tm.gogo.web.member.MemberDto;
import com.tm.gogo.web.response.ApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

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
                    .authority(Member.Authority.ROLE_MEMBER)
                    .type(type)
                    .location(location)
                    .build();

            memberRepository.saveAndFlush(member);

            // when
            MemberDto.Response memberResponse = memberService.findMemberByEmail(email);

            // then
            assertThat(memberResponse.getEmail()).isEqualTo(email);
            assertThat(memberResponse.getNickname()).isEqualTo(nickname);
            assertThat(memberResponse.getType()).isEqualTo(type);
            assertThat(memberResponse.getLocation().getName()).isEqualTo(locationName);
            assertThat(memberResponse.getLocation().getLatitude()).isEqualTo(latitude);
            assertThat(memberResponse.getLocation().getLongitude()).isEqualTo(longitude);
        }

        @Test
        @DisplayName("Id 로 회원 조회 성공")
        void testFindById() {
            // given
            String email = "woody@naver.net";
            String nickname = "woody";
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
                    .authority(Member.Authority.ROLE_MEMBER)
                    .type(type)
                    .location(location)
                    .build();

            memberRepository.saveAndFlush(member);

            // when
            MemberDto.Response memberResponse = memberService.findMemberById(member.getId());

            // then
            assertThat(memberResponse.getEmail()).isEqualTo(email);
            assertThat(memberResponse.getNickname()).isEqualTo(nickname);
            assertThat(memberResponse.getType()).isEqualTo(type);
            assertThat(memberResponse.getLocation().getName()).isEqualTo(locationName);
            assertThat(memberResponse.getLocation().getLatitude()).isEqualTo(latitude);
            assertThat(memberResponse.getLocation().getLongitude()).isEqualTo(longitude);
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
}
