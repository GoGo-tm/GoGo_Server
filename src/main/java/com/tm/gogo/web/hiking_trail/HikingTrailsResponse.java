package com.tm.gogo.web.hiking_trail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HikingTrailsResponse {

    private List<HikingTrailDto> contents;

    @Schema(example = "true", description = "다음 데이터가 있는지 없는지 표시")
    private boolean hasNext;

    public void updateContentFavorites(Set<Long> favoriteIds) {
        contents.forEach(content -> {
            boolean isFavorite = favoriteIds.contains(content.getId());
            content.updateFavorite(isFavorite);
        });
    }
}
