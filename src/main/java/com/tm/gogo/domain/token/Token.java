package com.tm.gogo.domain.token;

import com.tm.gogo.web.response.ApiException;
import com.tm.gogo.web.response.ErrorCode;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "token")
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

    @Column(name = "type")
    private Type type;

    public enum Type {
        REFRESH, NEW_PASSWORD
    }

    public boolean isNotEqualTo(String value) {
        return !StringUtils.equals(this.value, value);
    }

    public void renewValue(String value, LocalDateTime expiredAt) {
        this.value = value;
        this.expiredAt = expiredAt;
    }

    public void validateExpiredAt() {
        if (!expiredAt.isAfter(LocalDateTime.now())) {
            throw new ApiException(ErrorCode.EXPIRED_TOKEN, "토큰 만료 기간이 지났습니다. txId: " + key);
        }
    }
}
