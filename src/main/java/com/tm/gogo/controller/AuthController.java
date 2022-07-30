package com.tm.gogo.controller;

import com.tm.gogo.controller.dto.SignInDto;
import com.tm.gogo.controller.dto.SignUpDto;
import com.tm.gogo.controller.dto.TokenDto;
import com.tm.gogo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpDto.Response> signUp(@RequestBody SignUpDto.Request signUpDto) {
        return ResponseEntity.ok(authService.signUp(signUpDto));
    }

    @PostMapping("/signin")
    public ResponseEntity<TokenDto.Response> signIn(@RequestBody SignInDto.Request memberRequestDto) {
        return ResponseEntity.ok(authService.signIn(memberRequestDto));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto.Response> reissue(@RequestBody TokenDto.Request tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }
}
