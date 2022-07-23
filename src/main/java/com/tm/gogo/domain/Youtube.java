package com.tm.gogo.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "youtube")
public class Youtube {
    @Id @GeneratedValue
    @Column(name = "youtube_id")
    private Long id;

    @Column(name = "link")
    private String link;

    @Column(name = "youtube_theme")
    private Theme theme;

    private enum Theme{
        INFO, VLOG
    }
}
