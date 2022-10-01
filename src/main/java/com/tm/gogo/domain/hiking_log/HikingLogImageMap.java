package com.tm.gogo.domain.hiking_log;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HikingLogImageMap {
    private Map<Long, List<String>> imageUrls;
}
