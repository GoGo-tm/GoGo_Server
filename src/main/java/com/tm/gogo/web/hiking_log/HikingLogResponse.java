package com.tm.gogo.web.hiking_log;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HikingLogResponse {
    private List<HikingLogDto> contents;

    @Schema(example = "true", description = "다음 데이터가 있는지 없는지 표시")
    private boolean hasNext;
}
