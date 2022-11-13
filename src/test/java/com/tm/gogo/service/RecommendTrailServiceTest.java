package com.tm.gogo.service;

import com.tm.gogo.domain.hiking_trail.Difficulty;
import com.tm.gogo.domain.hiking_trail.HikingTrail;
import com.tm.gogo.domain.hiking_trail.HikingTrailRepository;
import com.tm.gogo.domain.recommend_trail.RecommendTrail;
import com.tm.gogo.domain.recommend_trail.RecommendTrailRepository;
import com.tm.gogo.domain.recommend_trail.RecommendTrailService;
import com.tm.gogo.domain.recommend_trail.RecommendTrailTheme;
import com.tm.gogo.web.recommend_trail.RecommendTrailResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class RecommendTrailServiceTest {

    @Autowired
    private RecommendTrailService recommendTrailService;

    @Autowired
    private RecommendTrailRepository recommendTrailRepository;

    @Autowired
    private HikingTrailRepository hikingTrailRepository;

    @Test
    @DisplayName("초보자 추천 등산로 조회")
    void test1() {
        // given
        for (int i = 0; i < 5; i++) {
            HikingTrail hikingTrail = createHikingTrail("등산로", 1000, Difficulty.EASY, 28, 32, "서울시 강남구 대치동");
            hikingTrailRepository.saveAndFlush(hikingTrail);

            RecommendTrail recommendTrail = RecommendTrail.builder()
                    .hikingTrail(hikingTrail)
                    .theme(RecommendTrailTheme.BEGINNER)
                    .build();

            recommendTrailRepository.saveAndFlush(recommendTrail);
        }


        for (int i = 0; i < 3; i++) {
            HikingTrail hikingTrail = createHikingTrail("등산로", 1000, Difficulty.EASY, 28, 32, "서울시 강남구 대치동");
            hikingTrailRepository.saveAndFlush(hikingTrail);

            RecommendTrail recommendTrail = RecommendTrail.builder()
                    .hikingTrail(hikingTrail)
                    .theme(RecommendTrailTheme.WINTER)
                    .build();

            recommendTrailRepository.saveAndFlush(recommendTrail);
        }

        // when
        RecommendTrailResponse response = recommendTrailService.findRecommendTrails(null, RecommendTrailTheme.BEGINNER);

        // then
        assertThat(response.getRecommendTrails().size()).isEqualTo(5);
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
