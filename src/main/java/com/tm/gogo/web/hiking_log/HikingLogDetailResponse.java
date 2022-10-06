package com.tm.gogo.web.hiking_log;

import com.tm.gogo.domain.hiking_log.HikingLog;
import com.tm.gogo.domain.hiking_trail.Difficulty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class HikingLogDetailResponse {
    private String hikingTrailName;
    private Integer starRating;
    private String address;
    private Difficulty difficulty;
    private Integer length;
    private LocalDateTime hikingDate;
    private List<String> hikingLogImageUrls;
    private String memo;

    public static HikingLogDetailResponse of(HikingLog hikingLog){
        return HikingLogDetailResponse.builder()
                .hikingTrailName(hikingLog.getHikingTrail().getName())
                .starRating(hikingLog.getStarRating())
                .address(hikingLog.getHikingTrail().getAddress())
                .difficulty(hikingLog.getHikingTrail().getDifficulty())
                .length(hikingLog.getHikingTrail().getLength())
                .hikingDate(hikingLog.getHikingDate())
                .hikingLogImageUrls(hikingLog.getHikingLogImageUrls())
                .memo(hikingLog.getMemo())
                .build();
    }
}
