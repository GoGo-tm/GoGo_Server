package com.tm.gogo.domain.hiking_log;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.tm.gogo.domain.hiking_log.QHikingLogImage.hikingLogImage;

@Repository
@RequiredArgsConstructor
public class HikingLogImageQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public void deleteHikingLogImages(Long hikingLogId) {
        jpaQueryFactory
                .delete(hikingLogImage)
                .where(
                        hikingLogImage.hikingLog.id.eq(hikingLogId)
                )
                .execute();
    }
}
