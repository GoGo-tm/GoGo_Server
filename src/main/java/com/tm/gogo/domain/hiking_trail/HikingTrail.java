package com.tm.gogo.domain.hiking_trail;

import com.tm.gogo.domain.Mountain;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Table(name = "hiking_trail")
public class HikingTrail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hiking_trail_id")
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

    @OneToMany(mappedBy = "hikingTrail", cascade = CascadeType.REMOVE)
    private List<Geometry> geometries = new ArrayList<>();

    enum Difficulty {
        EASY, NORMAL, HARD
    }
}