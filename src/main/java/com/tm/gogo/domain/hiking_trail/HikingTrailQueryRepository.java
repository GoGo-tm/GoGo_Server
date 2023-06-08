package com.tm.gogo.domain.hiking_trail;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tm.gogo.parameter.Scrollable;
import com.tm.gogo.web.hiking_trail.HikingTrailCondition;
import com.tm.gogo.web.hiking_trail.HikingTrailDto;
import com.tm.gogo.web.hiking_trail.HikingTrailsResponse;
import com.tm.gogo.web.hiking_trail.QHikingTrailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.tm.gogo.domain.favorite_trail.QFavoriteTrail.favoriteTrail;
import static com.tm.gogo.domain.hiking_trail.QHikingTrail.hikingTrail;

@Repository
@RequiredArgsConstructor
public class HikingTrailQueryRepository {

    private final JPAQueryFactory queryFactory;

    public HikingTrailsResponse findHikingTrails(HikingTrailCondition condition, Scrollable scrollable) {
        List<HikingTrailDto> results = queryFactory
                .select(new QHikingTrailDto(
                        hikingTrail.id,
                        hikingTrail.image.url,
                        hikingTrail.name,
                        hikingTrail.address,
                        hikingTrail.favoriteCount,
                        hikingTrail.difficulty,
                        hikingTrail.length
                ))
                .from(hikingTrail)
                .leftJoin(hikingTrail.image)
                .where(
                        greaterThanId(scrollable.getLastId()),
                        containsAddress(condition.getAddress()),
                        containsName(condition.getName()),
                        eqDifficulty(condition.getDifficulty()),
                        lessOrEqLength(condition.getLength()),
                        lessOrEqTime(condition.getTime())
                )
                .orderBy(condition.getOrder().getOrderSpecifier())
                .limit(scrollable.getSize() + 1)
                .fetch();

        return toResponse(results, scrollable);
    }

    public HikingTrailsResponse findFavoriteHikingTrails(Long memberId, HikingTrailCondition condition, Scrollable scrollable) {
        List<HikingTrailDto> results = queryFactory
                .select(new QHikingTrailDto(
                        hikingTrail.id,
                        hikingTrail.image.url,
                        hikingTrail.name,
                        hikingTrail.address,
                        hikingTrail.favoriteCount,
                        hikingTrail.difficulty,
                        hikingTrail.length
                ))
                .from(favoriteTrail)
                .join(favoriteTrail.hikingTrail, hikingTrail)
                .leftJoin(hikingTrail.image)
                .where(
                        favoriteTrail.member.id.eq(memberId),
                        greaterThanId(scrollable.getLastId()),
                        containsAddress(condition.getAddress()),
                        containsName(condition.getName()),
                        eqDifficulty(condition.getDifficulty()),
                        lessOrEqLength(condition.getLength()),
                        lessOrEqTime(condition.getTime())
                )
                .orderBy(condition.getOrder().getOrderSpecifier())
                .limit(scrollable.getSize() + 1)
                .fetch();

        return toResponse(results, scrollable);
    }

    private HikingTrailsResponse toResponse(List<HikingTrailDto> results, Scrollable scrollable) {
        boolean hasNext = results.size() > scrollable.getSize();
        // subList 요거 문제인 것 같음
        List<HikingTrailDto> contents = hasNext ? new ArrayList<>(results.subList(0, scrollable.getSize())) : results;
        return new HikingTrailsResponse(contents, hasNext);
    }

    private BooleanExpression greaterThanId(Long id) {
        return id != null ? hikingTrail.id.gt(id) : null;
    }

    private BooleanExpression containsAddress(String address) {
        return StringUtils.hasText(address) ? hikingTrail.address.contains(address) : null;
    }

    private BooleanExpression containsName(String name) {
        return StringUtils.hasText(name) ? hikingTrail.name.contains(name) : null;
    }

    private BooleanExpression eqDifficulty(Difficulty difficulty) {
        return difficulty != null ? hikingTrail.difficulty.eq(difficulty) : null;
    }

    private BooleanExpression lessOrEqLength(Integer length) {
        return length != null ? hikingTrail.length.loe(length) : null;
    }

    private BooleanExpression lessOrEqTime(Integer totalTime) {
        return totalTime != null ? hikingTrail.totalTime.loe(totalTime) : null;
    }
}
