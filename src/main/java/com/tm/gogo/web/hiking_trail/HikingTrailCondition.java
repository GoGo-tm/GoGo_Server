package com.tm.gogo.web.hiking_trail;

import com.tm.gogo.domain.hiking_trail.Difficulty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HikingTrailCondition {

    @Schema(example = "서울", description = "지역 (전국이면 null 값으로 요청)")
    private String address;

    @Schema(example = "EASY", description = "난이도 (EASY, NORMAL, HARD)")
    private Difficulty difficulty;

    @Schema(example = "2500", description = "단위: m - 구간거리 (길이)")
    private Integer length;

    @Schema(example = "120", description = "단위: 분 - 평균소요시간 (상행시간 + 하행시간)")
    private Integer time;

    @Schema(example = "POPULARITY", description = "정렬 기준 (POPULARITY, LONG, SHORT)")
    private HikingTrailOrder order;

    @Schema(example = "궁산", description = "산 이름으로 조회")
    private String name;

    public HikingTrailOrder getOrder() {
        return order != null ? order : HikingTrailOrder.POPULARITY;
    }
}
