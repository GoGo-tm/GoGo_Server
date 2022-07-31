package com.tm.gogo.web.auth;

import com.tm.gogo.domain.auth.AuthService;
import com.tm.gogo.web.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseDto<SignUpDto.Response> signUp(@RequestBody SignUpDto.Request signUpDto) {
        return ResponseDto.ok(authService.signUp(signUpDto));
    }

    @PostMapping("/signin")
    public ResponseDto<TokenDto.Response> signIn(@RequestBody SignInDto.Request memberRequestDto) {
        return ResponseDto.ok(authService.signIn(memberRequestDto));
    }

    @PostMapping("/reissue")
    public ResponseDto<TokenDto.Response> reissue(@RequestBody TokenDto.Request tokenRequestDto) {
        return ResponseDto.ok(authService.reissue(tokenRequestDto));
    }
}
