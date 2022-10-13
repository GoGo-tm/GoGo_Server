package com.tm.gogo.web.withdrawal_reason;

import com.tm.gogo.domain.withdrawal.Reason;
import com.tm.gogo.domain.withdrawal.WithdrawalReason;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalReasonDto {
    @Schema(example = "test", description = "Reason Enum 값", required = true)
    private Reason reason;

    @Schema(example = "의견", description = "맘에 안듭니다.")
    private String opinion;

    public WithdrawalReason toWithDrawlReason() {
        return WithdrawalReason.builder()
                .reason(reason)
                .opinion(opinion)
                .build();
    }
}
