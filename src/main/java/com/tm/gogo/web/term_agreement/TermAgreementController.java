package com.tm.gogo.web.term_agreement;

import com.tm.gogo.domain.term_agreement.TermAgreementService;
import com.tm.gogo.helper.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "/members/terms", description = "사용자 약관 동의 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/terms")
public class TermAgreementController {

    private final TermAgreementService termAgreementService;

    @Operation(summary = "특정 사용자 약관 조회하기", description = "사용자의 모든 약관과 동의 여부, 시간을 리턴")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "약관 조회 성공")
    })
    @GetMapping
    public ResponseEntity<List<TermAgreementResponse>> findTermAgreements() {
        return ResponseEntity.ok(
                termAgreementService.findTermAgreements(SecurityUtil.getCurrentMemberId())
        );
    }

    @Operation(summary = "특정 사용자 약관 업데이트", description = "원하는 약관 데이터와 true, false 여부를 전달하면 맞게 업데이트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "약관 업데이트 성공")
    })
    @PostMapping
    public ResponseEntity<Void> updateTermAgreement(@RequestBody TermAgreementRequest termAgreementRequest) {
        termAgreementService.updateTermAgreement(SecurityUtil.getCurrentMemberId(), termAgreementRequest.getTerm(), termAgreementRequest.getAgreed());
        return ResponseEntity.ok().build();
    }
}
