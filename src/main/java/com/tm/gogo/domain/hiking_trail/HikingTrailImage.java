package com.tm.gogo.domain.hiking_trail;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "hiking_trail_image")
public class HikingTrailImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hiking_trail_image_id")
    private Long id;

    @Column(name = "url")
    private String url;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hiking_trail_id")
    private HikingTrail hikingTrail;

    @Builder
    public HikingTrailImage(String url, HikingTrail hikingTrail) {
        this.url = url;
        this.hikingTrail = hikingTrail;
    }
}
