package com.tm.gogo.domain.notice;

import com.tm.gogo.parameter.Scrollable;
import com.tm.gogo.web.notice.NoticesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeQueryRepository noticeQueryRepository;

    public NoticesResponse findNotices(Scrollable scrollable) {
        return noticeQueryRepository.findNotices(scrollable);
    }
}
