package com.tm.gogo.service;

import com.tm.gogo.domain.favorite_trail.FavoriteTrail;
import com.tm.gogo.domain.favorite_trail.FavoriteTrailRepository;
import com.tm.gogo.domain.hiking_trail.*;
import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.member.MemberRepository;
import com.tm.gogo.parameter.Scrollable;
import com.tm.gogo.web.hiking_trail.*;
import com.tm.gogo.web.response.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class HikingTrailServiceTest {

    @Autowired
    private HikingTrailService hikingTrailService;

    @Autowired
    private HikingTrailRepository hikingTrailRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FavoriteTrailRepository favoriteTrailRepository;

    @Nested
    @DisplayName("등산로 조회")
    class FindTest {

        @BeforeEach
        void setup() {
            for (int i = 0; i < 10; i++) {
                HikingTrail hikingTrail = createHikingTrail("등산로", 1000, Difficulty.EASY, 28, 32, "서울시 강남구 대치동");
                hikingTrailRepository.saveAndFlush(hikingTrail);
            }
        }

        @Test
        @DisplayName("전체 등산로 리스트 조회")
        void testFindHikingTrails() {
            // when
            HikingTrailsResponse responses = hikingTrailService.findHikingTrails(new HikingTrailCondition(), new Scrollable());

            // then
            assertThat(responses.getContents().size()).isEqualTo(10);
        }

        @Test
        @DisplayName("필터링 - 등산로명 궁산")
        void testFindHikingTrailsFilterByName() {
            // given
            for (int i = 0; i < 5; i++) {
                HikingTrail hikingTrail = createHikingTrail("궁산", 0, Difficulty.EASY, 0, 0, "강원도 속초시 중앙동");
                hikingTrailRepository.saveAndFlush(hikingTrail);
            }

            // when
            HikingTrailsResponse allResponses = hikingTrailService.findHikingTrails(new HikingTrailCondition(), new Scrollable());
            HikingTrailsResponse responses = hikingTrailService.findHikingTrails(
                    HikingTrailCondition.builder().name("궁산").build(),
                    new Scrollable()
            );

            // then
            assertThat(allResponses.getContents().size()).isEqualTo(15);
            assertThat(responses.getContents().size()).isEqualTo(5);
        }

        @Test
        @DisplayName("필터링 - 지역 강원")
        void testFindHikingTrailsFilterByAddress() {
            // given
            for (int i = 0; i < 5; i++) {
                HikingTrail hikingTrail = createHikingTrail("등산로", 0, Difficulty.EASY, 0, 0, "강원도 속초시 중앙동");
                hikingTrailRepository.saveAndFlush(hikingTrail);
            }

            // when
            HikingTrailsResponse allResponses = hikingTrailService.findHikingTrails(new HikingTrailCondition(), new Scrollable());
            HikingTrailsResponse responses = hikingTrailService.findHikingTrails(
                    HikingTrailCondition.builder().address("강원").build(),
                    new Scrollable()
            );

            // then
            assertThat(allResponses.getContents().size()).isEqualTo(15);
            assertThat(responses.getContents().size()).isEqualTo(5);
        }

        @Test
        @DisplayName("필터링 - 난이도 어려움")
        void testFindHikingTrailsFilterByDifficulty() {
            // given
            for (int i = 0; i < 5; i++) {
                HikingTrail hikingTrail = createHikingTrail("등산로", 0, Difficulty.HARD, 0, 0, "서울시 강남구 대치동");
                hikingTrailRepository.saveAndFlush(hikingTrail);
            }

            // when
            HikingTrailsResponse allResponses = hikingTrailService.findHikingTrails(new HikingTrailCondition(), new Scrollable());
            HikingTrailsResponse responses = hikingTrailService.findHikingTrails(
                    HikingTrailCondition.builder().difficulty(Difficulty.HARD).build(),
                    new Scrollable()
            );

            // then
            assertThat(allResponses.getContents().size()).isEqualTo(15);
            assertThat(responses.getContents().size()).isEqualTo(5);
        }

        @Test
        @DisplayName("필터링 - 구간거리 0.5km")
        void testFindHikingTrailsFilterByLength() {
            // given
            for (int i = 0; i < 5; i++) {
                HikingTrail hikingTrail = createHikingTrail("등산로", 500, Difficulty.EASY, 0, 0, "강원도 속초시 중앙동");
                hikingTrailRepository.saveAndFlush(hikingTrail);
            }

            // when
            HikingTrailsResponse allResponses = hikingTrailService.findHikingTrails(new HikingTrailCondition(), new Scrollable());
            HikingTrailsResponse responses = hikingTrailService.findHikingTrails(
                    HikingTrailCondition.builder().length(500).build(),
                    new Scrollable()
            );

            // then
            assertThat(allResponses.getContents().size()).isEqualTo(15);
            assertThat(responses.getContents().size()).isEqualTo(5);
        }

        @Test
        @DisplayName("필터링 - 평균 소요시간 (상행속도 + 하행속도)")
        void testFindHikingTrailsFilterByTime() {
            // given
            for (int i = 0; i < 5; i++) {
                HikingTrail hikingTrail = createHikingTrail("등산로", 500, Difficulty.EASY, 14, 16, "강원도 속초시 중앙동");
                hikingTrailRepository.saveAndFlush(hikingTrail);
            }

            // when
            HikingTrailsResponse allResponses = hikingTrailService.findHikingTrails(new HikingTrailCondition(), new Scrollable());
            HikingTrailsResponse responses = hikingTrailService.findHikingTrails(
                    HikingTrailCondition.builder().time(30).build(),
                    new Scrollable()
            );

            // then
            assertThat(allResponses.getContents().size()).isEqualTo(15);
            assertThat(responses.getContents().size()).isEqualTo(5);
        }

        @Test
        @DisplayName("다중 필터링 - 지역 강원, 난이도 어려움")
        void testFindByMultipleFilter() {
            // given
            for (int i = 0; i < 5; i++) {
                HikingTrail hikingTrail = createHikingTrail("등산로", 0, Difficulty.HARD, 0, 0, "서울시 강남구 대치동");
                hikingTrailRepository.saveAndFlush(hikingTrail);
            }

            for (int i = 0; i < 3; i++) {
                HikingTrail hikingTrail = createHikingTrail("등산로", 0, Difficulty.HARD, 0, 0, "강원도 속초시 중앙동");
                hikingTrailRepository.saveAndFlush(hikingTrail);
            }

            // when
            HikingTrailsResponse allResponses = hikingTrailService.findHikingTrails(new HikingTrailCondition(), new Scrollable());
            HikingTrailsResponse hardResponses = hikingTrailService.findHikingTrails(
                    HikingTrailCondition.builder().difficulty(Difficulty.HARD).build(),
                    new Scrollable()
            );
            HikingTrailsResponse seoulResponses = hikingTrailService.findHikingTrails(
                    HikingTrailCondition.builder().address("서울").build(),
                    new Scrollable()
            );
            HikingTrailsResponse multiResponses = hikingTrailService.findHikingTrails(
                    HikingTrailCondition.builder().difficulty(Difficulty.HARD).address("서울").build(),
                    new Scrollable()
            );

            // then
            assertThat(allResponses.getContents().size()).isEqualTo(18);
            assertThat(hardResponses.getContents().size()).isEqualTo(8);
            assertThat(seoulResponses.getContents().size()).isEqualTo(15);
            assertThat(multiResponses.getContents().size()).isEqualTo(5);
        }

        @Test
        @DisplayName("정렬 - 인기순: 즐겨찾기 많이 된 순")
        void testFindOrderByFavorite() {
            // given
            for (int i = 1; i < 6; i++) {
                HikingTrail hikingTrail = createHikingTrail("등산로" + i, 0, Difficulty.HARD, 0, 0, "서울시 강남구 대치동");
                for (int j = 0; j < i; j++) {
                    hikingTrail.increaseFavoriteCount();
                }
                hikingTrailRepository.saveAndFlush(hikingTrail);
            }

            // when
            HikingTrailsResponse responses = hikingTrailService.findHikingTrails(
                    HikingTrailCondition.builder().order(HikingTrailOrder.POPULARITY).build(),
                    new Scrollable()
            );

            // then
            List<HikingTrailDto> contents = responses.getContents();
            for (int i = 0; i < 5; i++) {
                HikingTrailDto hikingTrailDto = contents.get(i);
                assertThat(hikingTrailDto.getName()).isEqualTo("등산로" + (5 - i));
            }
        }

        @Test
        @DisplayName("정렬 - 길이 짧은 순")
        void testFindOrderByLengthAsc() {
            // given
            for (int i = 1; i < 6; i++) {
                HikingTrail hikingTrail = createHikingTrail("등산로" + i, 1000 - i, Difficulty.HARD, 0, 0, "서울시 강남구 대치동");
                hikingTrailRepository.saveAndFlush(hikingTrail);
            }

            // when
            HikingTrailsResponse responses = hikingTrailService.findHikingTrails(
                    HikingTrailCondition.builder().order(HikingTrailOrder.SHORT).build(),
                    new Scrollable()
            );

            // then
            List<HikingTrailDto> contents = responses.getContents();
            for (int i = 0; i < 5; i++) {
                HikingTrailDto hikingTrailDto = contents.get(i);
                assertThat(hikingTrailDto.getName()).isEqualTo("등산로" + (5 - i));
            }
        }

        @Test
        @DisplayName("정렬 - 길이 긴순")
        void testFindOrderByLengthDesc() {
            // given
            for (int i = 1; i < 6; i++) {
                HikingTrail hikingTrail = createHikingTrail("등산로" + i, 1000 + i, Difficulty.HARD, 0, 0, "서울시 강남구 대치동");
                hikingTrailRepository.saveAndFlush(hikingTrail);
            }

            // when
            HikingTrailsResponse responses = hikingTrailService.findHikingTrails(
                    HikingTrailCondition.builder().order(HikingTrailOrder.LONG).build(),
                    new Scrollable()
            );

            // then
            List<HikingTrailDto> contents = responses.getContents();
            for (int i = 0; i < 5; i++) {
                HikingTrailDto hikingTrailDto = contents.get(i);
                assertThat(hikingTrailDto.getName()).isEqualTo("등산로" + (5 - i));
            }
        }
    }

    @Test
    @DisplayName("즐겨찾기 된 등산로 리스트 조회")
    void testFindFavorite() {
        // given
        Member member = new Member();
        memberRepository.saveAndFlush(member);

        for (int i = 0; i < 5; i++) {
            HikingTrail hikingTrail = createHikingTrail("등산로", 1000, Difficulty.EASY, 28, 32, "서울시 강남구 대치동");
            hikingTrailRepository.saveAndFlush(hikingTrail);
        }

        for (int i = 0; i < 3; i++) {
            HikingTrail hikingTrail = createHikingTrail("등산로 즐겨찾기", 1000, Difficulty.EASY, 28, 32, "서울시 강남구 대치동");
            hikingTrailRepository.saveAndFlush(hikingTrail);

            FavoriteTrail favoriteTrail = FavoriteTrail.builder().member(member).hikingTrail(hikingTrail).build();
            favoriteTrailRepository.saveAndFlush(favoriteTrail);
        }

        // when
        HikingTrailsResponse response = hikingTrailService.findFavoriteHikingTrails(member.getId(), new HikingTrailCondition(), new Scrollable());

        // then
        List<HikingTrailDto> contents = response.getContents();
        assertThat(contents.size()).isEqualTo(3);

        contents.forEach(content -> assertThat(content.getName()).isEqualTo("등산로 즐겨찾기"));
    }

    @Nested
    @DisplayName("등산로 하나 조회")
    class FindOne {

        @Test
        @DisplayName("조회 성공")
        void testFindOne() {
            // given
            String name = "관악산 A코스";
            Integer length = 123;
            Difficulty difficulty = Difficulty.EASY;
            Integer uptime = 1;
            Integer downtime = 2;
            String address = "서울시 강남구 대치동";
            String imageUrl = "mountain_image.jpg";
            String latitude = "37.22589972640079";
            String longitude = "128.44706436148968";

            HikingTrail hikingTrail = createHikingTrail(name, length, difficulty, uptime, downtime, address);

            HikingTrailImage image = HikingTrailImage.builder()
                    .url(imageUrl)
                    .hikingTrail(hikingTrail)
                    .build();
            hikingTrail.setImage(image);

            Geometry geometry = Geometry.builder()
                    .latitude(latitude)
                    .longitude(longitude)
                    .hikingTrail(hikingTrail)
                    .build();
            hikingTrail.addGeometry(geometry);

            hikingTrailRepository.saveAndFlush(hikingTrail);

            // when
            HikingTrailDetailResponse response = hikingTrailService.findHikingTrail(hikingTrail.getId());

            // then
            assertThat(response.getName()).isEqualTo(name);
            assertThat(response.getLength()).isEqualTo(length);
            assertThat(response.getDifficulty()).isEqualTo(difficulty);
            assertThat(response.getUptime()).isEqualTo(uptime);
            assertThat(response.getDowntime()).isEqualTo(downtime);
            assertThat(response.getAddress()).isEqualTo(address);
            assertThat(response.getImageUrl()).isEqualTo(imageUrl);

            GeometryDto geometryDto = response.getGeometries().stream().findFirst().orElse(null);
            assertThat(geometryDto).isNotNull();
            assertThat(geometryDto.getLatitude()).isEqualTo(latitude);
            assertThat(geometryDto.getLongitude()).isEqualTo(longitude);
        }

        @Test
        @DisplayName("등산로 데이터가 없어서 조회 실패")
        void testFindFail() {
            Long hikingTrailId = 1L;

            assertThatExceptionOfType(ApiException.class)
                    .isThrownBy(() -> hikingTrailService.findHikingTrail(hikingTrailId))
                    .withMessage("등산로 정보가 없습니다. hikingTrailId: " + hikingTrailId);
        }
    }

    private HikingTrail createHikingTrail(String name, Integer length, Difficulty difficulty, Integer uptime, Integer downtime, String address) {
        return HikingTrail.builder()
                .name(name)
                .length(length)
                .difficulty(difficulty)
                .uptime(uptime)
                .downtime(downtime)
                .address(address)
                .build();
    }
}
