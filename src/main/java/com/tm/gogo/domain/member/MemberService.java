package com.tm.gogo.domain.member;

import com.tm.gogo.web.member.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberDto.Response findMemberByEmail(String email){
        return memberRepository.findByEmail(email)
                .map(MemberDto.Response::of)
                .orElseThrow(() -> new NoSuchElementException("사용자 정보가 없습니다. email: " + email));
    }

    public MemberDto.Response findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .map(MemberDto.Response::of)
                .orElseThrow(() -> new NoSuchElementException("사용자 정보가 없습니다. memberId: " + memberId));
    }
}
