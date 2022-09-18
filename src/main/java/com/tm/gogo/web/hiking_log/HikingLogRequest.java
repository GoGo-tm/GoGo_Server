package com.tm.gogo.web.hiking_log;

import com.tm.gogo.domain.hiking_trail.HikingTrail;
import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.hiking_log.HikingLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HikingLogRequest {
    private LocalDateTime hikingDate;
    private Long hikingTrailId;
    private int starRating;
    private String memo;
    private List<MultipartFile> imageFiles;

    public HikingLog toHikingLog(Member member, HikingTrail hikingTrail, List<String> imageUrls) {
        return HikingLog.builder()
                .member(member)
                .hikingDate(hikingDate)
                .hikingTrail(hikingTrail)
                .starRating(starRating)
                .memo(memo)
                .imageUrls(imageUrls)
                .build();
    }

    public List<MultipartFile> getImageFiles() {
        return imageFiles != null ? imageFiles : Collections.emptyList();
    }
}
