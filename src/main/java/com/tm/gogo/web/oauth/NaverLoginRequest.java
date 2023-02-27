package com.tm.gogo.web.oauth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NaverLoginRequest {
    @Schema(example = "authorization_code")
    private String grantType;

    @Schema(example = "325b2ae4fab6a8fcafd9290b655434f2")
    private String clientId;

    @Schema(example = "1Ee8i2_T1j4Y0D7BXL7EDx6iLzLwes6V7Ans1wEFf_icqrFsssJ4wV1dXOEbLYBqSJfPGAo9dZwAAAGGgy4p6g")
    private String code;

    @Schema(example = "")
    private String state;
}
