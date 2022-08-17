package com.tm.gogo.domain.token;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class RefreshToken extends Token {

    @Builder
    public RefreshToken(String key, String value, LocalDateTime expiredAt) {
        super(key, value, expiredAt);
    }
}
