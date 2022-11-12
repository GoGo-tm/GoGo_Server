package com.tm.gogo.domain.recommend_trail;

import com.tm.gogo.domain.BaseEntity;
import com.tm.gogo.domain.hiking_trail.HikingTrail;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "recommend_trail", indexes = @Index(columnList = "theme"))
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
    private RecommendTrailTheme theme;

    @Builder
    public RecommendTrail(HikingTrail hikingTrail, RecommendTrailTheme theme) {
        this.hikingTrail = hikingTrail;
        this.theme = theme;
    }
}
