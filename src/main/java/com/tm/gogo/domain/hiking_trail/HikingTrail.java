package com.tm.gogo.domain.hiking_trail;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Table(name = "hiking_trail", indexes = @Index(columnList = "mountain_code", unique = true))
public class HikingTrail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hiking_trail_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "length")
    private Integer length;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty")
    private Difficulty difficulty;

    @Column(name = "uptime")
    private Integer uptime;

    @Column(name = "downtime")
    private Integer downtime;

    @Column(name = "mountain_code")
    private String mountainCode;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "hikingTrail", cascade = CascadeType.REMOVE)
    private List<Geometry> geometries = new ArrayList<>();

    enum Difficulty {
        EASY, NORMAL, HARD
    }
}
