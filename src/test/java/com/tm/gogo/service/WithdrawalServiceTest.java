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
import com.tm.gogo.domain.withdrawal.Reason;
import com.tm.gogo.domain.withdrawal.WithdrawalReasonRepository;
import com.tm.gogo.domain.withdrawal.WithdrawalService;
import com.tm.gogo.web.withdrawal_reason.WithdrawalReasonDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class WithdrawalServiceTest {
    @Autowired
    private HikingTrailRepository hikingTrailRepository;

    @Autowired
    private HikingLogRepository hikingLogRepository;

    @Autowired
    private FavoriteTrailRepository favoriteTrailRepository;

    @Autowired
    private WithdrawalReasonRepository withdrawalReasonRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WithdrawalService withdrawalService;

    @Autowired
    private MemberRepository memberRepository;

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
                .reason(Reason.REJOIN)
                .opinion("")
                .build();

        //when
        withdrawalService.withdrawal(member.getId(), reasonDto);

        //then
        assertThat(withdrawalReasonRepository.findAll()).isNotEmpty();
        assertThat(memberRepository.findAll()).isEmpty();
        assertThat(favoriteTrailRepository.findAll()).isEmpty();
        assertThat(hikingLogRepository.findAll()).isEmpty();
    }
}
