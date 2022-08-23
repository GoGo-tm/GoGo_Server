package com.tm.gogo.web.hikingLog;

import com.tm.gogo.domain.hiking_trail.HikingTrail;
import com.tm.gogo.domain.member.Member;
import com.tm.gogo.hikingLog.HikingLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HikingLogRequest {
    private Long memberId;
    private LocalDateTime hikingDate;
    private Long hikingTrailId;
    private int starRating;
    private String memo;

    public HikingLog toHikingLog(Member member, HikingTrail hikingTrail) {
        return HikingLog.builder()
                .member(member)
                .hikingDate(hikingDate)
                .hikingTrail(hikingTrail)
                .starRating(starRating)
                .memo(memo)
                .build();
    }
}
