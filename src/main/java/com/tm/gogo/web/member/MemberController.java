package com.tm.gogo.web.member;

import com.tm.gogo.domain.member.MemberService;
import com.tm.gogo.helper.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "/members", description = "회원 정보 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "내 정보 찾기", description = "별다른 파라미터 없이 Access Token 으로 내정보를 찾음")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "내 정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "내 정보가 존재하지 않음", content = @Content)
    })
    @GetMapping("/me")
    public ResponseEntity<MemberResponse> findMemberById() {
        return ResponseEntity.ok(memberService.findMemberById(SecurityUtil.getCurrentMemberId()));
    }

    @Operation(summary = "사용자 정보 찾기", description = "Email 값으로 사용자를 찾음")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 정보가 존재하지 않음", content = @Content)
    })
    @GetMapping("/{email}")
    public ResponseEntity<MemberResponse> findMemberByEmail(@PathVariable String email) {
        return ResponseEntity.ok(memberService.findMemberByEmail(email));
    }

    @PostMapping("/{email}/change-password")
    public ResponseEntity<Void> sendNewPasswordEmail(@PathVariable("email") String email){
        memberService.updatePasswordAndSendMail(email);
        return ResponseEntity.ok().build();
    }
}
