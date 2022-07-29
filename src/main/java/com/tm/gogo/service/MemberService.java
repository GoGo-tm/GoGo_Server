package com.tm.gogo.service;

import com.tm.gogo.controller.dto.MemberDto;
import com.tm.gogo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberDto.Response getMemberInfo(String email){
        return memberRepository.findByEmail(email)
                .map(MemberDto.Response::of)
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
    }

    @Transactional(readOnly = true)
    public MemberDto.Response findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .map(MemberDto.Response::of)
                .orElseThrow(() -> new RuntimeException("사용자 정보가 없습니다. memberId: " + memberId));
    }


}
