package com.tm.gogo.domain.withdrawal;

import com.tm.gogo.domain.favorite_trail.FavoriteTrailService;
import com.tm.gogo.domain.hiking_log.HikingLogService;
import com.tm.gogo.domain.member.MemberService;
import com.tm.gogo.web.withdrawal_reason.WithdrawalReasonDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WithdrawalService {
    private final WithdrawalReasonRepository withdrawalReasonRepository;
    private final HikingLogService hikingLogService;
    private final FavoriteTrailService favoriteTrailService;
    private final MemberService memberService;

    @Transactional
    public void withdrawal(Long memberId, WithdrawalReasonDto withdrawalReasonDto) {
        WithdrawalReason withdrawalReason = withdrawalReasonDto.toWithDrawlReason();
        withdrawalReasonRepository.save(withdrawalReason);

        hikingLogService.deleteAll(memberId);
        favoriteTrailService.deleteAll(memberId);
        memberService.delete(memberId);
    }
}
