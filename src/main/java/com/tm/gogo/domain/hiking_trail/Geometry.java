package com.tm.gogo.domain.hiking_trail;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "geometry")
public class Geometry {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "geometry_id")
    private Long id;

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "longitude")
    private Float longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hiking_trail_id")
    private HikingTrail hikingTrail;
}
