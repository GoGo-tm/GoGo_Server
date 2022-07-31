package com.tm.gogo.web.member;

import com.tm.gogo.helper.SecurityUtil;
import com.tm.gogo.domain.member.MemberService;
import com.tm.gogo.web.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseDto<MemberDto.Response> findMemberById() {
        return ResponseDto.ok(memberService.findMemberById(SecurityUtil.getCurrentMemberId()));
    }

    @GetMapping("/{email}")
    public ResponseDto<MemberDto.Response> findMemberByEmail(@PathVariable String email) {
        return ResponseDto.ok(memberService.findMemberByEmail(email));
    }
}
