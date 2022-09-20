package com.tm.gogo.domain.member;

import com.tm.gogo.domain.BaseEntity;
import com.tm.gogo.domain.favorite_trail.FavoriteTrail;
import com.tm.gogo.domain.term_agreement.Term;
import com.tm.gogo.domain.term_agreement.TermAgreement;
import com.tm.gogo.web.response.ApiException;
import com.tm.gogo.web.response.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "member", indexes = @Index(columnList = "email", unique = true))
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

    @OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    private final List<TermAgreement> termAgreements = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private final List<FavoriteTrail> favorites = new ArrayList<>();

    public void registerFavorite(FavoriteTrail favorite) {
        favorites.add(favorite);
    }

    public void deleteFavorite(FavoriteTrail favorite) {
        favorites.remove(favorite);
    }

    public enum Type {
        NATIVE, GOOGLE, NAVER, KAKAO
    }

    public enum Authority {
        ROLE_ADMIN, ROLE_MEMBER
    }

    @Builder
    public Member(String nickname, String email, Type type,String password, Authority authority, EnumSet<Term> terms) {
        this.nickname = nickname;
        this.email = email;
        this.type = type;
        this.password = password;
        this.authority = authority;
        initTermsAgreements(terms);
    }

    private void initTermsAgreements(EnumSet<Term> terms) {
        for (Term term : Term.values()) {
            TermAgreement agreements = TermAgreement.builder()
                    .member(this)
                    .term(term)
                    .agreed(CollectionUtils.containsInstance(terms, term))
                    .build();

            termAgreements.add(agreements);
        }
    }

    public void updatePassword(String password){
        this.password = password;
    }

    public void updateTermAgreed(Term term, Boolean agreed) {
        termAgreements.stream()
                .filter(agreement -> agreement.equalTerm(term))
                .findFirst()
                .orElseThrow(() -> new ApiException(ErrorCode.TERM_NOT_FOUND, "동의된 약관이 없습니다. memberId: " + id + ", term: " + term))
                .setAgreed(agreed);
    }

    public boolean isNotEquals(Long otherMemberId) {
        return !this.id.equals(otherMemberId);
    }
}
