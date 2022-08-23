package com.tm.gogo.domain;

import com.tm.gogo.domain.hiking_trail.HikingTrail;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "recommend_trail")
public class RecommendTrail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommend_trail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hiking_trail_id")
    private HikingTrail hikingTrail;

    @Column(name = "theme")
    @Enumerated(EnumType.STRING)
    private Theme theme;

    //TODO: 테마 입력
    public enum Theme {

    }
}
