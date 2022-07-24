package com.tm.gogo.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "youtube")
public class Youtube extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "youtube_id")
    private Long id;

    @Column(name = "link")
    private String link;

    @Column(name = "youtube_theme")
    @Enumerated(EnumType.STRING)
    private Theme theme;

    private enum Theme {
        INFO, VLOG
    }
}
