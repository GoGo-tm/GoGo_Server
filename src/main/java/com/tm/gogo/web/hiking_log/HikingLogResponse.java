package com.tm.gogo.web.hiking_log;

import com.tm.gogo.domain.hiking_log.HikingLogImageMap;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class HikingLogResponse {
    private List<HikingLogDto> contents;

    @Schema(example = "true", description = "다음 데이터가 있는지 없는지 표시")
    private boolean hasNext;

    public void setImageUrls(HikingLogImageMap hikingLogImageMap) {
        contents.forEach(hikingLogDto -> {
            List<String> urls = hikingLogImageMap.getImageUrls().get(hikingLogDto.getId());
            hikingLogDto.setImageUrls(urls);
        });
    }
}
