package com.tm.gogo.domain.token;

import com.tm.gogo.web.response.ApiException;
import com.tm.gogo.web.response.ErrorCode;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "token", indexes = @Index(columnList = "token_key", unique = true))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;

    @Column(name = "token_key")
    private String key;

    @Column(name = "token_value")
    private String value;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    public Token(String key, String value, LocalDateTime expiredAt) {
        this.key = key;
        this.value = value;
        this.expiredAt = expiredAt;
    }

    public boolean isNotEqualTo(String value) {
        return !StringUtils.equals(this.value, value);
    }

    public void renewValue(String value, LocalDateTime expiredAt) {
        this.value = value;
        this.expiredAt = expiredAt;
    }

    public void validateExpiredAt() {
        if (this.expiredAt.isBefore(LocalDateTime.now())) {
            throw new ApiException(ErrorCode.EXPIRED_TOKEN, "토큰 만료 기간이 지났습니다. txId: " + key);
        }
    }

    public void validateValue(String value) {
        if (isNotEqualTo(value)) {
            throw new ApiException(ErrorCode.INVALID_TOKEN_VALUE, "토큰 값이 다릅니다. value: " + value);
        }
    }
}
