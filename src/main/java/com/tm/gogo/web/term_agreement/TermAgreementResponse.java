package com.tm.gogo.web.term_agreement;

import com.tm.gogo.domain.term_agreement.Term;
import com.tm.gogo.domain.term_agreement.TermAgreement;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@Builder
public class TermAgreementResponse {

    @Schema(example = "PRIVACY_POLICY", description = "약관 이름")
    private Term term;

    @Schema(example = "true", description = "약관 동의 여부")
    private Boolean agreed;

    @Schema(example = "2022-12-25 12:12:12", description = "약관 변경 시각")
    private LocalDateTime updatedAt;

    public static TermAgreementResponse of(TermAgreement termAgreement) {
        return TermAgreementResponse.builder()
                .term(termAgreement.getTerm())
                .agreed(termAgreement.getAgreed())
                .updatedAt(termAgreement.getUpdatedAt())
                .build();
    }
}
