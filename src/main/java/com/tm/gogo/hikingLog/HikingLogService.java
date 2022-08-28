package com.tm.gogo.hikingLog;

import com.tm.gogo.domain.hiking_trail.HikingTrail;
import com.tm.gogo.domain.hiking_trail.HikingTrailRepository;
import com.tm.gogo.domain.member.Member;
import com.tm.gogo.domain.member.MemberRepository;
import com.tm.gogo.web.hikingLog.HikingLogRequest;
import com.tm.gogo.web.response.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tm.gogo.web.response.ErrorCode.HIKING_TRAIL_NOT_FOUND;
import static com.tm.gogo.web.response.ErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class HikingLogService {
    private final HikingLogRepository hikingLogRepository;
    private final HikingTrailRepository hikingTrailRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createHikingLog(Long memberId, HikingLogRequest hikingLogRequest) {
        Member member = findMemberByMemberId(memberId);
        HikingTrail hikingTrail = findByHikingTrailId(hikingLogRequest.getHikingTrailId());

        HikingLog hikingLog = hikingLogRequest.toHikingLog(member, hikingTrail);

        hikingLogRepository.save(hikingLog);
        return hikingLog.getId();
    }

    private Member findMemberByMemberId(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(MEMBER_NOT_FOUND, "사용자 정보가 없습니다. memberId: " + memberId));
    }

    private HikingTrail findByHikingTrailId(Long hikingTrailId) {
        return hikingTrailRepository.findById(hikingTrailId)
                .orElseThrow(() -> new ApiException(HIKING_TRAIL_NOT_FOUND, "등산로 정보가 없습니다. hikingTrailId: " + hikingTrailId));
    }
}
