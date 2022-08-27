package com.tm.gogo.domain.term_agreement;

import com.tm.gogo.domain.BaseEntity;
import com.tm.gogo.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "term_agreement", uniqueConstraints = { @UniqueConstraint(columnNames = { "term", "member_id" }) })
public class TermAgreement extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "term_agreement_id")
    private Long id;

    @Column(name = "term")
    @Enumerated(EnumType.STRING)
    private Term term;

    @Column(name = "agreed")
    private Boolean agreed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public TermAgreement(Member member, Term term, Boolean agreed) {
        this.term = term;
        this.agreed = agreed;
        this.member = member;
    }

    public boolean equalTerm(Term term) {
        return this.term == term;
    }

    public void setAgreed(Boolean agreed) {
        this.agreed = agreed;
    }
}
