package com.tm.gogo.domain.youtube;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tm.gogo.parameter.Scrollable;
import com.tm.gogo.web.youtube.QYoutubeDto;
import com.tm.gogo.web.youtube.YoutubeDto;
import com.tm.gogo.web.youtube.YoutubesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.tm.gogo.domain.youtube.QYoutube.youtube;

@Repository
@RequiredArgsConstructor
public class YoutubeQueryRepository {

    private final JPAQueryFactory queryFactory;

    public YoutubesResponse findYoutubes(Scrollable scrollable) {
        List<YoutubeDto> results = queryFactory
                .select(new QYoutubeDto(
                        youtube.id,
                        youtube.link,
                        youtube.channelName,
                        youtube.description,
                        youtube.contact,
                        youtube.theme
                ))
                .from(youtube)
                .where(
                        greaterThanId(scrollable.getLastId())
                )
                .limit(scrollable.getSize() + 1)
                .fetch();

        return toResponse(results, scrollable);
    }

    private YoutubesResponse toResponse(List<YoutubeDto> results, Scrollable scrollable) {
        boolean hasNext = results.size() > scrollable.getSize();
        List<YoutubeDto> contents = hasNext ? new ArrayList<>(results.subList(0, scrollable.getSize())) : results;
        return new YoutubesResponse(contents, hasNext);
    }

    private BooleanExpression greaterThanId(Long id) {
        return id != null ? youtube.id.gt(id) : null;
    }
}
