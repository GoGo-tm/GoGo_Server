package com.tm.gogo.domain;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Authority authority;

    private enum Type {
        NATIVE, GOOGLE, NAVER, KAKAO
    }

    public enum Authority {
        ROLE_ADMIN, ROLE_MEMBER
    }

    @Builder
    public Member(String email, String password, Authority authority) {
        this.email = email;
        this.password = password;
        this.authority = authority;
    }

}
