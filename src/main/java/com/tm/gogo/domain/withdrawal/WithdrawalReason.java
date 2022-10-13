package com.tm.gogo.domain.withdrawal;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class WithdrawalReason {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "reason")
    @Enumerated(EnumType.STRING)
    private Reason reason;

    @Column(name = "opinion")
    private String opinion;

    @Builder
    public WithdrawalReason(Reason reason, String opinion) {
        this.reason = reason;
        this.opinion = opinion;
    }
}
