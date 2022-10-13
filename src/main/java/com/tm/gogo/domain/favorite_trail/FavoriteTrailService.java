package com.tm.gogo.domain.favorite_trail;

import com.tm.gogo.domain.hiking_trail.HikingTrail;
import com.tm.gogo.domain.hiking_trail.HikingTrailRepository;
import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.member.MemberService;
import com.tm.gogo.web.response.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tm.gogo.web.response.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteTrailService {

    private final MemberService memberService;
    private final HikingTrailRepository hikingTrailRepository;
    private final FavoriteTrailRepository favoriteTrailRepository;

    public void registerFavorite(Long memberId, Long hikingTrailId) {
        Member member = memberService.findMemberById(memberId);
        HikingTrail hikingTrail = findHikingTrail(hikingTrailId);
        validate(member, hikingTrail);

        FavoriteTrail favorite = FavoriteTrail.builder().member(member).hikingTrail(hikingTrail).build();
        favoriteTrailRepository.save(favorite);
    }

    private void validate(Member member, HikingTrail hikingTrail) {
        favoriteTrailRepository.findByMemberAndHikingTrail(member, hikingTrail)
                .ifPresent(favorite -> {
                    throw new ApiException(ALREADY_REGISTER_FAVORITE_TRAIL, "이미 즐겨찾기 된 상태입니다. favoriteId: " + favorite.getId());
                });
    }

    public void deleteFavorite(Long memberId, Long hikingTrailId) {
        Member member = memberService.findMemberById(memberId);
        HikingTrail hikingTrail = findHikingTrail(hikingTrailId);
        FavoriteTrail favorite = favoriteTrailRepository.findByMemberAndHikingTrail(member, hikingTrail)
                .orElseThrow(() -> new ApiException(FAVORITE_TRAIL_NOT_FOUND, "즐겨찾기 된 상태가 아닙니다. memberId: " + memberId + ", hikingTrailId: " + hikingTrailId));

        favorite.destroy();
        favoriteTrailRepository.delete(favorite);
    }

    @Transactional
    public void deleteAll(Long memberId) {
        Member member = memberService.findMemberById(memberId);
        favoriteTrailRepository.deleteAllByMember(member);
    }

    private HikingTrail findHikingTrail(Long hikingTrailId) {
        return hikingTrailRepository.findById(hikingTrailId)
                .orElseThrow(() -> new ApiException(HIKING_TRAIL_NOT_FOUND, "등산로 정보가 없습니다. hikingTrailId: " + hikingTrailId));
    }
}
