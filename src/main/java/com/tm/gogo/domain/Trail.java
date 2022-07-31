package com.tm.gogo.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "trail")
public class Trail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trail_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "trail_number")
    private Integer trailNumber;

    @Column(name = "length")
    private Integer length;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty")
    private Difficulty difficulty;

    @Column(name = "uptime")
    private Integer uptime;

    @Column(name = "downtime")
    private Integer downtime;

    @Column(name = "geometry")
    private String geometry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mountain_id")
    private Mountain mountain;

    enum Difficulty {
        EASY, NORMAL, HARD
    }
}
