package com.tm.gogo.web.youtube;

import com.querydsl.core.annotations.QueryProjection;
import com.tm.gogo.domain.youtube.YoutubeTheme;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class YoutubeDto {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "https://youtu.be/test")
    private String link;

    @Schema(example = "gogo 채널")
    private String channelName;

    @Schema(example = "이 영상은 gogo 채널의 테스트 동영상입니다.")
    private String description;

    @Schema(example = "asdf@gmail.com")
    private String contact;

    @Schema(example = "INFO")
    private YoutubeTheme theme;

    @QueryProjection
    public YoutubeDto(Long id, String link, String channelName, String description, String contact, YoutubeTheme theme) {
        this.id = id;
        this.link = link;
        this.channelName = channelName;
        this.description = description;
        this.contact = contact;
        this.theme = theme;
    }
}
