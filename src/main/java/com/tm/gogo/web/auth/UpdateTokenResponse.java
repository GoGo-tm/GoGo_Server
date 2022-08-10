package com.tm.gogo.web.auth;

import com.tm.gogo.domain.token.Token;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateTokenResponse {
    private String txId;

    public static UpdateTokenResponse of(Token token){
        return UpdateTokenResponse.builder()
                .txId(token.getKey())
                .build();
    }
}
