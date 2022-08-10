package com.tm.gogo.web.auth;

import com.tm.gogo.domain.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "/auth", description = "인증 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "가입하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "가입 성공하면 이메일 리턴"),
            @ApiResponse(responseCode = "409", description = "이미 가입한 회원이 존재하는 경우", content = @Content)
    })
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest signUpDto) {
        return ResponseEntity.ok(authService.signUp(signUpDto));
    }


    @Operation(summary = "로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공하면 토큰 리턴"),
    })
    @PostMapping("/signin")
    public ResponseEntity<TokenResponse> signIn(@RequestBody SignInRequest memberRequestDto) {
        return ResponseEntity.ok(authService.signIn(memberRequestDto));
    }


    @Operation(summary = "토큰 재발급")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Access Token 과 Refresh Token 을 재발급 해줌"),
    })
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(@RequestBody TokenRequest tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }
    @PostMapping("/issue/{email}")
    public ResponseEntity<UpdateTokenResponse> issueToken(@PathVariable("email") String email){
        return ResponseEntity.ok(authService.issueToken(email));
    }
}
