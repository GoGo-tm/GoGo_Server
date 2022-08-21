package com.tm.gogo.web.hiking_trail;

import com.querydsl.core.types.OrderSpecifier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.tm.gogo.domain.hiking_trail.QHikingTrail.hikingTrail;

@Getter
@RequiredArgsConstructor
public enum HikingTrailOrder {
    POPULARITY(hikingTrail.favoriteCount.desc()),
    LONG(hikingTrail.length.desc()),
    SHORT(hikingTrail.length.asc())
    ;

    private final OrderSpecifier<?> orderSpecifier;
}
