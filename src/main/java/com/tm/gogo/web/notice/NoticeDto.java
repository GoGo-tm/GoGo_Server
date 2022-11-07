package com.tm.gogo.web.notice;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeDto {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "공지사항 제목")
    private String title;

    @Schema(example = "공지사항 내용")
    private String content;

    @Schema(example = "notice.jpg")
    private String imageUrl;

    @QueryProjection
    public NoticeDto(Long id, String title, String content, String imageUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }
}
