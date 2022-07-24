package com.tm.gogo.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "recommend_trail")
public class RecommendTrail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommend_trail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trail_id")
    private Trail trail;

    @Column(name = "theme")
    @Enumerated(EnumType.STRING)
    private Theme theme;

    //TODO: 테마 입력
    private enum Theme {

    }
}
