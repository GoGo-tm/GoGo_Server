package com.tm.gogo.service;

import com.tm.gogo.domain.youtube.Youtube;
import com.tm.gogo.domain.youtube.YoutubeRepository;
import com.tm.gogo.domain.youtube.YoutubeService;
import com.tm.gogo.parameter.Scrollable;
import com.tm.gogo.web.youtube.YoutubesResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class YoutubeServiceTest {

    @Autowired
    private YoutubeService youtubeService;

    @Autowired
    private YoutubeRepository youtubeRepository;

    @Test
    @DisplayName("유튜브 조회")
    void test() {
        // given
        int size = 5;
        for (int i = 0; i < size; i++) {
            String link = "link" + i;
            String channelName = "channel" + i;
            String description = "description" + i;
            String contact = "contact" + i;

            Youtube youtube = Youtube.builder()
                    .link(link)
                    .channelName(channelName)
                    .description(description)
                    .contact(contact)
                    .build();

            youtubeRepository.saveAndFlush(youtube);
        }

        // when
        YoutubesResponse response = youtubeService.findYoutubes(new Scrollable());

        // then
        assertThat(response.getYoutubes().size()).isEqualTo(size);
        assertThat(response.isHasNext()).isEqualTo(false);
    }
}
