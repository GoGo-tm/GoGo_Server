package com.tm.gogo.hikingLog;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "hiking_log_image")
public class HikingLogImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hiking_log_image_id")
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "number")
    private Integer number;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "hiking_log_id")
    private HikingLog hikingLog;

    @Builder
    public HikingLogImage(String url, Integer number, HikingLog hikingLog) {
        this.url = url;
        this.number = number;
        this.hikingLog = hikingLog;
    }
}
