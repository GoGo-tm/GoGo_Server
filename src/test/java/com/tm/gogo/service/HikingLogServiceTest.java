package com.tm.gogo.service;

import com.tm.gogo.HikingTrailRepository;
import com.tm.gogo.domain.hiking_trail.HikingTrail;
import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.member.MemberRepository;
import com.tm.gogo.hikingLog.HikingLog;
import com.tm.gogo.hikingLog.HikingLogRepository;
import com.tm.gogo.hikingLog.HikingLogService;
import com.tm.gogo.web.hikingLog.HikingLogRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
@Transactional
public class HikingLogServiceTest {

    @Autowired
    private HikingLogService hikingLogService;

    @Autowired
    private HikingLogRepository hikingLogRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HikingTrailRepository hikingTrailRepository;

    @DisplayName("HikingLog 생성 성공")
    @Test
    void testCreateHikingLog() {
        //given
        Member member = new Member();
        memberRepository.saveAndFlush(member);

        HikingTrail hikingTrail = new HikingTrail();
        hikingTrailRepository.saveAndFlush(hikingTrail);

        HikingLogRequest hikingLogRequest = HikingLogRequest.builder()
                .hikingTrailId(hikingTrail.getId())
                .hikingDate(LocalDateTime.now())
                .starRating(5)
                .memo("재밌당")
                .build();

        //when
        Long hikingLogId = hikingLogService.createHikingLog(1L, hikingLogRequest);

        //then
        Optional<HikingLog> hikingLog = hikingLogRepository.findById(hikingLogId);
        Assertions.assertThat(hikingLog).isNotEmpty();
    }
}
