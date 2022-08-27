package com.tm.gogo.web.term_agreement;

import com.tm.gogo.domain.term_agreement.Term;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TermAgreementRequest {

    @Schema(example = "PRIVACY_POLICY", description = "약관 이름")
    private Term term;

    @Schema(example = "true", description = "약관 동의 여부")
    private Boolean agreed;
}
