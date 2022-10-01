package com.tm.gogo.service;

import com.tm.gogo.domain.hiking_log.*;
import com.tm.gogo.domain.hiking_trail.Difficulty;
import com.tm.gogo.domain.hiking_trail.HikingTrail;
import com.tm.gogo.domain.hiking_trail.HikingTrailRepository;
import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.member.MemberRepository;
import com.tm.gogo.parameter.Scrollable;
import com.tm.gogo.web.hiking_log.HikingLogDetailResponse;
import com.tm.gogo.web.hiking_log.HikingLogDto;
import com.tm.gogo.web.hiking_log.HikingLogRequest;
import com.tm.gogo.web.hiking_log.HikingLogResponse;
import com.tm.gogo.web.response.ApiException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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
    @DisplayName("등산 로그 리스트 조회 성공")
    void testFindHikingLogs() {
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
        Assertions.assertThat(hikingLogDto.getImageUrls()).isEqualTo(imageUrls);
    }

    @Test
    @DisplayName("등산 기록 하나 조회 성공")
    void testFindHikingLog() {
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

        HikingLog hikingLog = HikingLog.builder()
                .member(member)
                .hikingTrail(hikingTrail)
                .hikingDate(LocalDateTime.now())
                .starRating(5)
                .imageUrls(imageUrls)
                .memo("이미지 넣기 술법")
                .build();

        hikingLogRepository.saveAndFlush(hikingLog);

        //when
        HikingLogDetailResponse response = hikingLogService.findHikingLog(hikingLog.getId());

        Assertions.assertThat(response.getMemo()).isEqualTo("이미지 넣기 술법");
        Assertions.assertThat(response.getDifficulty()).isEqualTo(Difficulty.EASY);
        Assertions.assertThat(response.getAddress()).isEqualTo("서울시 강남구 대치동");
        Assertions.assertThat(response.getHikingLogImageUrls()).isEqualTo(imageUrls);

    }


    @Nested
    @DisplayName("등산 로그 삭제")
    class testDelete {
        Long memberId;
        Long hikingLogId;
        @BeforeEach
        void setup() {
            //given
            Member member = new Member();
            memberRepository.saveAndFlush(member);
            memberId = member.getId();

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

            HikingLog hikingLog = HikingLog.builder()
                    .member(member)
                    .hikingTrail(hikingTrail)
                    .hikingDate(LocalDateTime.now())
                    .starRating(5)
                    .imageUrls(imageUrls)
                    .memo("이미지 넣기 술법")
                    .build();

            hikingLogRepository.saveAndFlush(hikingLog);
            hikingLogId = hikingLog.getId();
        }
        @Test
        void testDelete()  {
            hikingLogService.deleteHikingLog(memberId, hikingLogId);
            Assertions.assertThat(hikingLogRepository.findAll()).isEmpty();
        }
    }
    @Test
    @DisplayName("등산로그 수정 성공")
    void testUpdateHikingLog() {
        //given
        Member member = new Member();
        memberRepository.saveAndFlush(member);

        HikingTrail hikingTrail1 = HikingTrail.builder()
                .name("등산로")
                .length(1000)
                .difficulty(Difficulty.EASY)
                .uptime(28)
                .downtime(32)
                .address("서울시 강남구 대치동")
                .build();
        hikingTrailRepository.saveAndFlush(hikingTrail1);
        HikingTrail hikingTrail2 = HikingTrail.builder()
                .name("등산로")
                .length(1000)
                .difficulty(Difficulty.EASY)
                .uptime(28)
                .downtime(32)
                .address("서울시 강남구 대치동")
                .build();
        hikingTrailRepository.saveAndFlush(hikingTrail2);

        List<String> imageUrls1 = new ArrayList<>();
        imageUrls1.add("12");
        imageUrls1.add("123");
        imageUrls1.add("1234");
        imageUrls1.add("12345");
        imageUrls1.add("123456");

        List<String> imageUrls2 = new ArrayList<>();
        imageUrls2.add("12");
        imageUrls2.add("123");
        imageUrls2.add("123");
        imageUrls2.add("123");
        imageUrls2.add("123");

        HikingLog hikingLog = HikingLog.builder()
                .member(member)
                .hikingTrail(hikingTrail1)
                .hikingDate(LocalDateTime.now())
                .starRating(5)
                .imageUrls(imageUrls1)
                .memo("이미지 넣기 술법")
                .build();
        hikingLogRepository.saveAndFlush(hikingLog);

        HikingLogRequest hikingLogRequest = HikingLogRequest.builder()
                .hikingTrailId(hikingTrail2.getId())
                .hikingDate(LocalDateTime.now())
                .starRating(3)
                .imageUrls(imageUrls2)
                .memo("수정하기 술법")
                .build();

        //when
        Long hikingLogId = hikingLogService.updateHikingLog(member.getId(), hikingLog.getId(), hikingLogRequest);

        //then
        HikingLogDetailResponse response = hikingLogService.findHikingLog(hikingLogId);

        Assertions.assertThat(response.getMemo()).isEqualTo("수정하기 술법");
        Assertions.assertThat(response.getDifficulty()).isEqualTo(Difficulty.EASY);
        Assertions.assertThat(response.getAddress()).isEqualTo("서울시 강남구 대치동");
        Assertions.assertThat(response.getStarRating()).isEqualTo(3);
        Assertions.assertThat(response.getHikingLogImageUrls()).isEqualTo(imageUrls2);
    }

    @Test
    @DisplayName("등산로그 수정 멤버가 달라서 실패")
    void testUpdateHikingLogFail() {
        //given
        Member member1 = new Member();
        memberRepository.saveAndFlush(member1);

        Member member2 = new Member();
        memberRepository.saveAndFlush(member2);

        HikingTrail hikingTrail1 = HikingTrail.builder()
                .name("등산로")
                .length(1000)
                .difficulty(Difficulty.EASY)
                .uptime(28)
                .downtime(32)
                .address("서울시 강남구 대치동")
                .build();
        hikingTrailRepository.saveAndFlush(hikingTrail1);
        HikingTrail hikingTrail2 = HikingTrail.builder()
                .name("등산로")
                .length(1000)
                .difficulty(Difficulty.EASY)
                .uptime(28)
                .downtime(32)
                .address("서울시 강남구 대치동")
                .build();
        hikingTrailRepository.saveAndFlush(hikingTrail2);

        List<String> imageUrls1 = new ArrayList<>();
        imageUrls1.add("12");
        imageUrls1.add("123");
        imageUrls1.add("1234");
        imageUrls1.add("12345");
        imageUrls1.add("123456");

        List<String> imageUrls2 = new ArrayList<>();
        imageUrls2.add("12");
        imageUrls2.add("123");
        imageUrls2.add("123");
        imageUrls2.add("123");
        imageUrls2.add("123");

        HikingLog hikingLog = HikingLog.builder()
                .member(member1)
                .hikingTrail(hikingTrail1)
                .hikingDate(LocalDateTime.now())
                .starRating(5)
                .imageUrls(imageUrls1)
                .memo("이미지 넣기 술법")
                .build();
        hikingLogRepository.saveAndFlush(hikingLog);

        HikingLogRequest hikingLogRequest = HikingLogRequest.builder()
                .hikingTrailId(hikingTrail2.getId())
                .hikingDate(LocalDateTime.now())
                .starRating(3)
                .imageUrls(imageUrls2)
                .memo("수정하기 술법")
                .build();

        //then
        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> hikingLogService.updateHikingLog(member2.getId(), hikingLog.getId(), hikingLogRequest))
                .withMessage("memberId 값이 다릅니다. memberId: " + member2.getId());
    }
}
