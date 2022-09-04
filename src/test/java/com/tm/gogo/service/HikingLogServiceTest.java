package com.tm.gogo.service;

import com.tm.gogo.domain.hiking_log.HikingLog;
import com.tm.gogo.domain.hiking_log.HikingLogImage;
import com.tm.gogo.domain.hiking_log.HikingLogRepository;
import com.tm.gogo.domain.hiking_log.HikingLogService;
import com.tm.gogo.domain.hiking_trail.Difficulty;
import com.tm.gogo.domain.hiking_trail.HikingTrail;
import com.tm.gogo.domain.hiking_trail.HikingTrailRepository;
import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.member.MemberRepository;
import com.tm.gogo.parameter.Scrollable;
import com.tm.gogo.web.hiking_log.HikingLogDto;
import com.tm.gogo.web.hiking_log.HikingLogRequest;
import com.tm.gogo.web.hiking_log.HikingLogResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Long hikingLogId = hikingLogService.createHikingLog(member.getId(), hikingLogRequest);

        //then
        Optional<HikingLog> hikingLog = hikingLogRepository.findById(hikingLogId);
        Assertions.assertThat(hikingLog).isNotEmpty();
    }

    @DisplayName("이미지 5개 추가 된 HikingLog 생성 성공")
    @Test
    void testCreateHikingLogImage() {
        //given
        Member member = new Member();
        memberRepository.saveAndFlush(member);

        HikingTrail hikingTrail = new HikingTrail();
        hikingTrailRepository.saveAndFlush(hikingTrail);

        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("12");
        imageUrls.add("123");
        imageUrls.add("1234");
        imageUrls.add("12345");
        imageUrls.add("123456");

        HikingLogRequest hikingLogRequest = HikingLogRequest.builder()
                .hikingTrailId(hikingTrail.getId())
                .hikingDate(LocalDateTime.now())
                .starRating(5)
                .imageUrls(imageUrls)
                .memo("이미지 넣기 술법")
                .build();

        //when
        Long hikingLogId = hikingLogService.createHikingLog(member.getId(), hikingLogRequest);

        //then
        Optional<HikingLog> optionalHikingLog = hikingLogRepository.findById(hikingLogId);
        Assertions.assertThat(optionalHikingLog).isNotEmpty();

        HikingLog hikingLog = optionalHikingLog.get();

        List<String> urls = hikingLog.getHikingLogImages().stream()
                .map(HikingLogImage::getUrl)
                .collect(Collectors.toList());

        Assertions.assertThat(urls).isEqualTo(imageUrls);
    }

    @Test
    @DisplayName("등산 로그 조회")
    void testFindHikingLogList() {
        //given
        Member member = new Member();
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

        HikingLogRequest hikingLogRequest = HikingLogRequest.builder()
                .hikingTrailId(hikingTrail.getId())
                .hikingDate(LocalDateTime.now())
                .starRating(5)
                .imageUrls(imageUrls)
                .memo("이미지 넣기 술법")
                .build();

        hikingLogService.createHikingLog(member.getId(), hikingLogRequest);

        //when
        HikingLogResponse response = hikingLogService.findHikingLogs(member.getId(), new Scrollable());

        //then
        List<HikingLogDto> contents = response.getContents();
        HikingLogDto hikingLogDto = contents.get(0);

        Assertions.assertThat(hikingLogDto).isNotNull();
        Assertions.assertThat(hikingLogDto.getHikingTrailName()).isEqualTo("등산로");
        Assertions.assertThat(hikingLogDto.getStarRating()).isEqualTo(5);
        Assertions.assertThat(hikingLogDto.getAddress()).isEqualTo("서울시 강남구 대치동");
        Assertions.assertThat(hikingLogDto.getLength()).isEqualTo(1000);
        Assertions.assertThat(hikingLogDto.getDifficulty()).isEqualTo(Difficulty.EASY);
    }
}
