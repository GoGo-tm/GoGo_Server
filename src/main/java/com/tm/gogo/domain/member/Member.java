package com.tm.gogo.domain.member;

import com.tm.gogo.domain.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "member")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Authority authority;

    public enum Type {
        NATIVE, GOOGLE, NAVER, KAKAO
    }

    public enum Authority {
        ROLE_ADMIN, ROLE_MEMBER
    }

    @Builder
    public Member(String nickname, String email, Type type,String password, Authority authority) {
        this.nickname = nickname;
        this.email = email;
        this.type = type;
        this.password = password;
        this.authority = authority;
    }

    public void updatePassword(String password){
        this.password = password;
    }

}
