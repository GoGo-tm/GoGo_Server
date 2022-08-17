package com.tm.gogo.web.auth;

import com.tm.gogo.domain.token.Token;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateTokenDto {
    private String txId;
    private String email;

    public static UpdateTokenDto of(Token token){
        return UpdateTokenDto.builder()
                .txId(token.getKey())
                .email(token.getValue())
                .build();
    }
}
