package com.tm.gogo.domain.hiking_trail;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
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
    private Integer length; // m 단위

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty")
    private Difficulty difficulty;

    @Column(name = "uptime")
    private Integer uptime; // 분

    @Column(name = "downtime")
    private Integer downtime;   // 분

    @Column(name = "total_time")
    private Integer totalTime;  // 분

    @Column(name = "mountain_code")
    private String mountainCode;

    @Column(name = "address")
    private String address;

    @Column(name = "favorite_count")
    private Long favoriteCount;

    @OneToOne(mappedBy = "hikingTrail", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private HikingTrailImage image;

    @OneToMany(mappedBy = "hikingTrail", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private final List<Geometry> geometries = new ArrayList<>();

    @Builder
    public HikingTrail(String name, Integer length, Difficulty difficulty, Integer uptime, Integer downtime, String address) {
        this.name = name;
        this.length = length;
        this.difficulty = difficulty;
        this.uptime = uptime;
        this.downtime = downtime;
        this.totalTime = uptime + downtime;
        this.address = address;
        this.favoriteCount = 0L;
    }

    public void setImage(HikingTrailImage image) {
        this.image = image;
    }

    public void addGeometry(Geometry geometry) {
        this.geometries.add(geometry);
    }

    public String getImageUrl() {
        return image != null ? image.getUrl() : null;
    }

    public void increaseFavoriteCount() {
        this.favoriteCount += 1;
    }
}
