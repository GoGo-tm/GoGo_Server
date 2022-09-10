package com.tm.gogo.domain.hiking_trail;

import com.tm.gogo.domain.favorite_trail.FavoriteTrail;
import com.tm.gogo.domain.favorite_trail.FavoriteTrailRepository;
import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.member.MemberRepository;
import com.tm.gogo.parameter.Scrollable;
import com.tm.gogo.web.hiking_trail.HikingTrailCondition;
import com.tm.gogo.web.hiking_trail.HikingTrailDetailResponse;
import com.tm.gogo.web.hiking_trail.HikingTrailDto;
import com.tm.gogo.web.hiking_trail.HikingTrailsResponse;
import com.tm.gogo.web.response.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.tm.gogo.web.response.ErrorCode.HIKING_TRAIL_NOT_FOUND;
import static com.tm.gogo.web.response.ErrorCode.MEMBER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HikingTrailService {

    private final HikingTrailQueryRepository hikingTrailQueryRepository;
    private final HikingTrailRepository hikingTrailRepository;
    private final MemberRepository memberRepository;
    private final FavoriteTrailRepository favoriteTrailRepository;

    public HikingTrailsResponse findHikingTrails(HikingTrailCondition condition, Scrollable scrollable) {
        return hikingTrailQueryRepository.findHikingTrails(condition, scrollable);
    }

    public HikingTrailsResponse findHikingTrailsOfMember(Long memberId, HikingTrailCondition condition, Scrollable scrollable) {
        HikingTrailsResponse hikingTrails = hikingTrailQueryRepository.findHikingTrails(condition, scrollable);
        updateFavorite(memberId, hikingTrails);
        return hikingTrails;
    }

    private void updateFavorite(Long memberId, HikingTrailsResponse hikingTrails) {
        Member member = findMember(memberId);

        List<Long> hikingTrailIds = hikingTrails.getContents().stream()
                .map(HikingTrailDto::getId)
                .collect(Collectors.toList());

        List<HikingTrail> trails = hikingTrailRepository.findAllById(hikingTrailIds);

        Set<Long> favoriteTrailIds = favoriteTrailRepository.findByMemberAndHikingTrailIn(member, trails).stream()
                .map(FavoriteTrail::getHikingTrail)
                .map(HikingTrail::getId)
                .collect(Collectors.toSet());

        hikingTrails.updateContentFavorites(favoriteTrailIds);
    }

    public HikingTrailsResponse findFavoriteHikingTrails(Long memberId, HikingTrailCondition condition, Scrollable scrollable) {
        return hikingTrailQueryRepository.findFavoriteHikingTrails(memberId, condition, scrollable);
    }

    public HikingTrailDetailResponse findHikingTrail(Long hikingTrailId) {
        return hikingTrailRepository.findById(hikingTrailId)
                .map(HikingTrailDetailResponse::of)
                .orElseThrow(() -> new ApiException(HIKING_TRAIL_NOT_FOUND, "등산로 정보가 없습니다. hikingTrailId: " + hikingTrailId));
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(MEMBER_NOT_FOUND, "사용자 정보가 없습니다. memberId: " + memberId));
    }
}
