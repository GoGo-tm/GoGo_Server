package com.tm.gogo.web.member;

import com.tm.gogo.domain.member.CommandMemberService;
import com.tm.gogo.domain.member.ChangePasswordService;
import com.tm.gogo.domain.member.QueryMemberService;
import com.tm.gogo.domain.withdrawal.WithdrawalService;
import com.tm.gogo.helper.SecurityUtil;
import com.tm.gogo.web.auth.UpdateTokenDto;
import com.tm.gogo.web.withdrawal_reason.WithdrawalReasonDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "/members", description = "회원 정보 API")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final ChangePasswordService changePasswordService;
    private final QueryMemberService queryMemberService;
    private final CommandMemberService commandMemberService;
    private final WithdrawalService withdrawalService;

    @Operation(summary = "내 정보 찾기", description = "별다른 파라미터 없이 Access Token 으로 내정보를 찾음")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "내 정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "내 정보가 존재하지 않음", content = @Content)
    })
    @GetMapping("/me")
    public ResponseEntity<MemberResponse> findMemberById() {
        return ResponseEntity.ok(queryMemberService.findMemberInfoById(SecurityUtil.getCurrentMemberId()));
    }

    @Operation(summary = "비밀번호 찾기 토큰 발급 요청", description = "토큰 검증을 위해 토큰 발행")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토큰 발행 성공"),
            @ApiResponse(responseCode = "404", description = "잘못된 파라미터", content = @Content)
    })
    @PostMapping("/{email}/token")
    public ResponseEntity<UpdateTokenDto> issueToken(@PathVariable("email") String email) {
        return ResponseEntity.ok(changePasswordService.issueToken(email));
    }

    @Operation(summary = "비밀번호 찾기", description = "비밀번호 변경 후 email로 바뀐 비밀번호 전송")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 후 email 보내기 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 정보가 존재하지 않음", content = @Content)
    })
    @PostMapping("/change-password")
    public ResponseEntity<Void> sendNewPasswordEmail(@RequestBody UpdateTokenDto updateTokenDto) {
        changePasswordService.updatePasswordAndSendMail(updateTokenDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "내 정보 수정", description = "수정하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 정보가 존재하지 않음", content = @Content)
    })
    @PutMapping()
    public ResponseEntity<Void> updateMember(@RequestBody MemberRequest memberRequest) {
        commandMemberService.update(SecurityUtil.getCurrentMemberId(), memberRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "탈퇴하기", description = "사용자 탈퇴")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "탈퇴 성공")
    })
    @DeleteMapping()
    public ResponseEntity<Void> withdrawal(@RequestBody WithdrawalReasonDto withdrawalReasonDto) {
        withdrawalService.withdrawal(SecurityUtil.getCurrentMemberId(), withdrawalReasonDto);
        return ResponseEntity.ok().build();
    }
}
