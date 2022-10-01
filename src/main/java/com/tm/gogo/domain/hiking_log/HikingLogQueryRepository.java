package com.tm.gogo.domain.hiking_log;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tm.gogo.parameter.Scrollable;
import com.tm.gogo.web.hiking_log.HikingLogDto;
import com.tm.gogo.web.hiking_log.HikingLogResponse;
import com.tm.gogo.web.hiking_log.QHikingLogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tm.gogo.domain.hiking_log.QHikingLog.hikingLog;
import static com.tm.gogo.domain.hiking_trail.QHikingTrail.hikingTrail;

@Repository
@RequiredArgsConstructor
public class HikingLogQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public HikingLogResponse findHikingLogs(Long memberId, Scrollable scrollable) {
        List<HikingLogDto> results = jpaQueryFactory
                .select(new QHikingLogDto(
                        hikingLog.id,
                        hikingTrail.name,
                        hikingTrail.image.url,
                        hikingLog.hikingDate,
                        hikingLog.starRating,
                        hikingTrail.difficulty,
                        hikingTrail.address,
                        hikingTrail.length
                ))
                .from(hikingLog)
                .join(hikingLog.hikingTrail, hikingTrail)
                .leftJoin(hikingTrail.image)
                .where(
                        hikingLog.member.id.eq(memberId),
                        lessThanId(scrollable.getLastId())
                )
                .orderBy(hikingLog.id.desc())
                .limit(scrollable.getSize() + 1)
                .fetch();

        return toResponse(results, scrollable);
    }

    public void deleteHikingLog(Long hikingLogId) {
        jpaQueryFactory
                .delete(hikingLog)
                .where(
                        hikingLog.id.eq(hikingLogId)
                )
                .execute();
    }

    private HikingLogResponse toResponse(List<HikingLogDto> results, Scrollable scrollable) {
        boolean hasNext = results.size() > scrollable.getSize();
        List<HikingLogDto> contents = hasNext ? results.subList(0, scrollable.getSize()) : results;
        return new HikingLogResponse(contents, hasNext);
    }

    private BooleanExpression lessThanId(Long id) {return id != null ? hikingLog.id.lt(id) : null;}
}
