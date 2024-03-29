package com.tm.gogo.domain.hiking_trail;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "geometry")
public class Geometry {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "geometry_id")
    private Long id;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hiking_trail_id")
    private HikingTrail hikingTrail;

    @Builder
    public Geometry(String latitude, String longitude, HikingTrail hikingTrail) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.hikingTrail = hikingTrail;
    }
}
