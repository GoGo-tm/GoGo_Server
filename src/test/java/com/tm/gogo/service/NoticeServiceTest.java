package com.tm.gogo.service;

import com.tm.gogo.domain.notice.Notice;
import com.tm.gogo.domain.notice.NoticeRepository;
import com.tm.gogo.domain.notice.NoticeService;
import com.tm.gogo.parameter.Scrollable;
import com.tm.gogo.web.notice.NoticesResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class NoticeServiceTest {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private NoticeRepository noticeRepository;

    @Test
    @DisplayName("공지사항 조회")
    void test() {
        // given
        int size = 5;
        for (int i = 0; i < size; i++) {
            String title = "제목" + i;
            String content = "내용" + i;
            String image = "image.jpg" + i;
            Notice notice = new Notice(title, content, image);
            noticeRepository.saveAndFlush(notice);
        }


        // when
        NoticesResponse response = noticeService.findNotices(new Scrollable());

        // then
        assertThat(response.getNotices().size()).isEqualTo(size);
        assertThat(response.isHasNext()).isEqualTo(false);
    }
}
