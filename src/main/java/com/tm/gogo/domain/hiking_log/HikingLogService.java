package com.tm.gogo.domain.hiking_log;

import com.tm.gogo.domain.hiking_trail.HikingTrail;
import com.tm.gogo.domain.hiking_trail.HikingTrailRepository;
import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.member.MemberService;
import com.tm.gogo.parameter.Scrollable;
import com.tm.gogo.web.hiking_log.HikingLogDetailResponse;
import com.tm.gogo.web.hiking_log.HikingLogDto;
import com.tm.gogo.web.hiking_log.HikingLogRequest;
import com.tm.gogo.web.hiking_log.HikingLogResponse;
import com.tm.gogo.web.response.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.tm.gogo.web.response.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class HikingLogService {
    private final MemberService memberService;
    private final HikingLogRepository hikingLogRepository;
    private final HikingTrailRepository hikingTrailRepository;
    private final HikingLogQueryRepository hikingLogQueryRepository;
    private final HikingLogImageQueryRepository hikingLogImageQueryRepository;

    @Transactional
    public Long createHikingLog(Long memberId, HikingLogRequest hikingLogRequest) {
        Member member = memberService.findMemberById(memberId);
        HikingTrail hikingTrail = findByHikingTrailId(hikingLogRequest.getHikingTrailId());

        HikingLog hikingLog = hikingLogRequest.toHikingLog(member, hikingTrail);

        hikingLogRepository.save(hikingLog);
        return hikingLog.getId();
    }

    private HikingTrail findByHikingTrailId(Long hikingTrailId) {
        return hikingTrailRepository.findById(hikingTrailId)
                .orElseThrow(() -> new ApiException(HIKING_TRAIL_NOT_FOUND, "등산로 정보가 없습니다. hikingTrailId: " + hikingTrailId));
    }

    @Transactional(readOnly = true)
    public HikingLogResponse findHikingLogs(Long memberId, Scrollable scrollable) {
        HikingLogResponse hikingLogs = hikingLogQueryRepository.findHikingLogs(memberId, scrollable);
        setImageUrls(hikingLogs);
        return hikingLogs;
    }

    private void setImageUrls(HikingLogResponse hikingLogs) {
        List<Long> hikingLogIds = hikingLogs.getContents().stream()
                .map(HikingLogDto::getId)
                .collect(Collectors.toList());

        Map<Long, List<String>> map = hikingLogRepository.findAllById(hikingLogIds).stream()
                .collect(Collectors.toMap(HikingLog::getId, HikingLog::getHikingLogImageUrls));

        HikingLogImageMap hikingLogImageMap = HikingLogImageMap.builder().imageUrls(map).build();

        hikingLogs.setImageUrls(hikingLogImageMap);
    }

    @Transactional(readOnly = true)
    public HikingLogDetailResponse findHikingLog(Long hikingLogId) {
        return hikingLogRepository.findById(hikingLogId)
                .map(HikingLogDetailResponse::of)
                .orElseThrow(() -> new ApiException(HIKING_LOG_NOT_FOUND, "등산로그 정보가 없습니다. hikingLogId: " + hikingLogId));
    }

    @Transactional
    public void deleteHikingLog(Long memberId, Long hikingLogId) {
        HikingLog hikingLog = findById(hikingLogId);
        Member member = hikingLog.getMember();

        member.validate(memberId);

        hikingLogImageQueryRepository.deleteHikingLogImages(hikingLogId);
        hikingLogQueryRepository.deleteHikingLog(hikingLogId);
    }

    private HikingLog findById(Long hikingLogId) {
        return hikingLogRepository.findById(hikingLogId)
                .orElseThrow(() -> new ApiException(HIKING_LOG_NOT_FOUND, "등산로그 정보가 없습니다. hikingLogId: " + hikingLogId));
    }

    @Transactional
    public Long updateHikingLog(Long memberId, Long hikingLogId, HikingLogRequest hikingLogRequest) {
        HikingLog hikingLog = findById(hikingLogId);
        Member member = hikingLog.getMember();

        member.validate(memberId);

        HikingTrail hikingTrail = findByHikingTrailId(hikingLogRequest.getHikingTrailId());
        hikingLog.update(hikingLogRequest, hikingTrail);

        return hikingLogId;
    }

    @Transactional
    public void deleteAll(Long memberId) {
        List<Long> hikingLogIds = hikingLogQueryRepository.findIds(memberId);

        hikingLogImageQueryRepository.deleteAllByHikingLogIds(hikingLogIds);
        hikingLogQueryRepository.deleteAllByIds(hikingLogIds);
    }
}
