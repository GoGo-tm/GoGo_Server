package com.tm.gogo.domain.youtube;

import com.tm.gogo.parameter.Scrollable;
import com.tm.gogo.web.youtube.YoutubesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class YoutubeService {

    private final YoutubeQueryRepository youtubeQueryRepository;

    public YoutubesResponse findYoutubes(Scrollable scrollable) {
        return youtubeQueryRepository.findYoutubes(scrollable);
    }
}
