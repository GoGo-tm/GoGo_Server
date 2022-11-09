package com.tm.gogo.domain.notice;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tm.gogo.parameter.Scrollable;
import com.tm.gogo.web.notice.NoticeDto;
import com.tm.gogo.web.notice.NoticesResponse;
import com.tm.gogo.web.notice.QNoticeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tm.gogo.domain.notice.QNotice.notice;

@Repository
@RequiredArgsConstructor
public class NoticeQueryRepository {

    private final JPAQueryFactory queryFactory;

    public NoticesResponse findNotices(Scrollable scrollable) {
        List<NoticeDto> results = queryFactory
                .select(new QNoticeDto(
                        notice.id,
                        notice.title,
                        notice.content,
                        notice.image
                ))
                .from(notice)
                .where(
                        greaterThanId(scrollable.getLastId())
                )
                .limit(scrollable.getSize() + 1)
                .fetch();

        return toResponse(results, scrollable);
    }

    private NoticesResponse toResponse(List<NoticeDto> results, Scrollable scrollable) {
        boolean hasNext = results.size() > scrollable.getSize();
        List<NoticeDto> contents = hasNext ? results.subList(0, scrollable.getSize()) : results;
        return new NoticesResponse(contents, hasNext);
    }

    private BooleanExpression greaterThanId(Long id) {
        return id != null ? notice.id.gt(id) : null;
    }
}
