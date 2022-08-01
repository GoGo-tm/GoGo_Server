package com.tm.gogo.web.member;

import com.tm.gogo.helper.SecurityUtil;
import com.tm.gogo.domain.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<MemberDto.Response> findMemberById() {
        return ResponseEntity.ok(memberService.findMemberById(SecurityUtil.getCurrentMemberId()));
    }

    @GetMapping("/{email}")
    public ResponseEntity<MemberDto.Response> findMemberByEmail(@PathVariable String email) {
        return ResponseEntity.ok(memberService.findMemberByEmail(email));
    }
}
