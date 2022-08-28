package com.tm.gogo.parameter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Scrollable {

    @Schema(example = "0", description = "마지막으로 조회한 ID")
    private Long lastId;

    @Schema(example = "3", description = "가져오려는 갯수")
    private Integer size;

    public int getSize() {
        return size != null ? size : 20;
    }
}
