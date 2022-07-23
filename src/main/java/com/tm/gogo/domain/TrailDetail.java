package com.tm.gogo.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "trail_detail")
public class TrailDetail {
    @Id @GeneratedValue
    @Column(name = "trail_detail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trail_id")
    private Trail trail;

    @Column(name = "trail_number")
    private Integer trailNumber;

    @Column(name = "length")
    private Integer length;

    @Column(name = "difficulty")
    private Difficulty difficulty;
    private enum Difficulty{
        EASY, NORMAL, HARD
    }

    @Column(name = "uptime")
    private Integer uptime;

    @Column(name = "downtime")
    private Integer downtime;

    @Column(name = "geometry")
    private String geometry;
}
