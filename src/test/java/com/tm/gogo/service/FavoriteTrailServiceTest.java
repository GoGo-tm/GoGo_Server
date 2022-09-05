package com.tm.gogo.service;

import com.tm.gogo.domain.favorite_trail.FavoriteTrail;
import com.tm.gogo.domain.favorite_trail.FavoriteTrailRepository;
import com.tm.gogo.domain.favorite_trail.FavoriteTrailService;
import com.tm.gogo.domain.hiking_trail.Difficulty;
import com.tm.gogo.domain.hiking_trail.HikingTrail;
import com.tm.gogo.domain.hiking_trail.HikingTrailRepository;
import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.member.MemberRepository;
import com.tm.gogo.web.response.ApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
public class FavoriteTrailServiceTest {

    @Autowired
    private FavoriteTrailService favoriteTrailService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HikingTrailRepository hikingTrailRepository;

    @Autowired
    private FavoriteTrailRepository favoriteTrailRepository;

    @Nested
    @DisplayName("즐겨찾기 등록")
    class RegisterFavoriteTest {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Member member = createMember();
            HikingTrail hikingTrail = createHikingTrail("등산로", 1000, Difficulty.EASY, 28, 32, "서울시 강남구 대치동");
            assertThat(hikingTrail.getFavoriteCount()).isEqualTo(0);

            // when
            favoriteTrailService.registerFavorite(member.getId(), hikingTrail.getId());

            // then
            List<FavoriteTrail> favorites = favoriteTrailRepository.findAll();
            assertThat(favorites.size()).isEqualTo(1);

            FavoriteTrail favorite = favorites.get(0);
            assertThat(favorite.getMember()).isEqualTo(member);
            assertThat(favorite.getHikingTrail()).isEqualTo(hikingTrail);
            assertThat(hikingTrail.getFavoriteCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("이미 즐겨찾기 되어 있으면 실패")
        void fail1() {
            // given
            Member member = createMember();
            HikingTrail hikingTrail = createHikingTrail("등산로", 1000, Difficulty.EASY, 28, 32, "서울시 강남구 대치동");

            // when
            favoriteTrailService.registerFavorite(member.getId(), hikingTrail.getId());

            // then
            assertThatExceptionOfType(ApiException.class)
                    .isThrownBy(() -> favoriteTrailService.registerFavorite(member.getId(), hikingTrail.getId()))
                    .withMessageStartingWith("이미 즐겨찾기 된 상태입니다. favoriteId: ");
            assertThat(hikingTrail.getFavoriteCount()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("즐겨찾기 해제")
    class DeleteFavoriteTest {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Member member = createMember();
            HikingTrail hikingTrail = createHikingTrail("등산로", 1000, Difficulty.EASY, 28, 32, "서울시 강남구 대치동");
            FavoriteTrail favorite = FavoriteTrail.builder().member(member).hikingTrail(hikingTrail).build();
            favoriteTrailRepository.saveAndFlush(favorite);
            assertThat(favoriteTrailRepository.findAll().size()).isEqualTo(1);
            assertThat(hikingTrail.getFavoriteCount()).isEqualTo(1);

            // when
            favoriteTrailService.deleteFavorite(member.getId(), hikingTrail.getId());

            // then
            List<FavoriteTrail> favorites = favoriteTrailRepository.findAll();
            assertThat(favorites).isEmpty();
            assertThat(hikingTrail.getFavoriteCount()).isEqualTo(0);
        }

        @Test
        @DisplayName("즐겨찾기 되어 있지 않은 상태면 실패")
        void fail1() {
            // given
            Member member = createMember();
            HikingTrail hikingTrail = createHikingTrail("등산로", 1000, Difficulty.EASY, 28, 32, "서울시 강남구 대치동");

            // then
            assertThatExceptionOfType(ApiException.class)
                    .isThrownBy(() -> favoriteTrailService.deleteFavorite(member.getId(), hikingTrail.getId()))
                    .withMessage("즐겨찾기 된 상태가 아닙니다. memberId: " + member.getId() + ", hikingTrailId: " + hikingTrail.getId());
            assertThat(hikingTrail.getFavoriteCount()).isEqualTo(0);
        }
    }

    private Member createMember() {
        Member member = new Member();
        return memberRepository.saveAndFlush(member);
    }

    private HikingTrail createHikingTrail(String name, Integer length, Difficulty difficulty, Integer uptime, Integer downtime, String address) {
        HikingTrail hikingTrail = HikingTrail.builder()
                .name(name)
                .length(length)
                .difficulty(difficulty)
                .uptime(uptime)
                .downtime(downtime)
                .address(address)
                .build();

        return hikingTrailRepository.saveAndFlush(hikingTrail);
    }
}
