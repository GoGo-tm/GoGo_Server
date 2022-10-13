package com.tm.gogo.service;

import com.tm.gogo.domain.favorite_trail.FavoriteTrail;
import com.tm.gogo.domain.favorite_trail.FavoriteTrailRepository;
import com.tm.gogo.domain.hiking_log.HikingLog;
import com.tm.gogo.domain.hiking_log.HikingLogRepository;
import com.tm.gogo.domain.hiking_trail.Difficulty;
import com.tm.gogo.domain.hiking_trail.HikingTrail;
import com.tm.gogo.domain.hiking_trail.HikingTrailRepository;
import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.member.MemberRepository;
import com.tm.gogo.domain.member.MemberService;
import com.tm.gogo.domain.term_agreement.Term;
import com.tm.gogo.domain.withdrawal_reason.Reason;
import com.tm.gogo.domain.withdrawal_reason.WithdrawalReasonRepository;
import com.tm.gogo.helper.RandomPasswordGenerator;
import com.tm.gogo.web.member.MemberRequest;
import com.tm.gogo.web.member.MemberResponse;
import com.tm.gogo.web.response.ApiException;
import com.tm.gogo.web.withdrawal_reason.WithdrawalReasonDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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
    private HikingTrailRepository hikingTrailRepository;

    @Autowired
    private HikingLogRepository hikingLogRepository;

    @Autowired
    private FavoriteTrailRepository favoriteTrailRepository;

    @Autowired
    private WithdrawalReasonRepository withdrawalReasonRepository;

    @Nested
    @DisplayName("회원 조회")
    class FindTest {
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
            MemberResponse memberResponse = memberService.findMemberInfoById(member.getId());

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
                    .isThrownBy(() -> memberService.findMemberInfoById(memberId))
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
        memberService.updatePassword(member.getEmail(), newPassword);

        // then
        Member updatedMember = memberRepository.findByEmail(email).get();
        boolean matches = passwordEncoder.matches(newPassword, updatedMember.getPassword());
        assertThat(matches).isTrue();
    }

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

        memberService.update(member.getId(), memberRequest);

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
                .isThrownBy(() -> memberService.update(member.getId(), memberRequest))
                .withMessage("password 값이 다릅니다. password: " + memberRequest.getPassword());
    }

    @Test
    @DisplayName("탈퇴")
    void testWithdrawal() {
        //given
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

        HikingTrail hikingTrail = HikingTrail.builder()
                .name("등산로")
                .length(1000)
                .difficulty(Difficulty.EASY)
                .uptime(28)
                .downtime(32)
                .address("서울시 강남구 대치동")
                .build();
        hikingTrailRepository.saveAndFlush(hikingTrail);

        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("12");
        imageUrls.add("123");
        imageUrls.add("1234");
        imageUrls.add("12345");
        imageUrls.add("123456");

        List<String> imageUrls2 = new ArrayList<>();
        imageUrls.add("12");
        imageUrls.add("123");
        imageUrls.add("1234");
        imageUrls.add("12345");
        imageUrls.add("123456");

        HikingLog hikingLog1 = HikingLog.builder()
                .member(member)
                .hikingTrail(hikingTrail)
                .hikingDate(LocalDateTime.now())
                .starRating(5)
                .imageUrls(imageUrls)
                .memo(" ")
                .build();
        hikingLogRepository.saveAndFlush(hikingLog1);

        HikingLog hikingLog2 = HikingLog.builder()
                .member(member)
                .hikingTrail(hikingTrail)
                .hikingDate(LocalDateTime.now())
                .starRating(5)
                .imageUrls(imageUrls2)
                .memo(" ")
                .build();
        hikingLogRepository.saveAndFlush(hikingLog2);

        FavoriteTrail favorite = FavoriteTrail.builder().member(member).hikingTrail(hikingTrail).build();
        favoriteTrailRepository.saveAndFlush(favorite);

        WithdrawalReasonDto reasonDto = WithdrawalReasonDto.builder()
                .reason(Reason.TEST)
                .opinion("")
                .build();

        //when
        memberService.withdrawal(member.getId(), reasonDto);

        //then
        assertThat(withdrawalReasonRepository.findAll()).isNotEmpty();
        assertThat(memberRepository.findAll()).isEmpty();
        assertThat(favoriteTrailRepository.findAll()).isEmpty();
        assertThat(hikingLogRepository.findAll()).isEmpty();

    }
}
