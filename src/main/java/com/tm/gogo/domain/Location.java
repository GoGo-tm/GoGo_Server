package com.tm.gogo.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Getter
@Entity
@NoArgsConstructor
@Table(name = "location")
public class Location extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "longitude")
    private Float longitude;

    @Builder
    public Location(String name, Float latitude, Float longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
