package com.tm.gogo.web.notice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticesResponse {

    private List<NoticeDto> notices;

    @Schema(example = "true", description = "다음 데이터가 있는지 없는지 표시")
    private boolean hasNext;
}
