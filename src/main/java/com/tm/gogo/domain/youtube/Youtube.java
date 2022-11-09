package com.tm.gogo.domain.youtube;

import com.tm.gogo.domain.BaseEntity;
import lombok.Builder;
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

    @Column(name = "channel_name")
    private String channelName;

    @Column(name = "description")
    private String description;

    @Column(name = "contact")
    private String contact;

    @Column(name = "youtube_theme")
    @Enumerated(EnumType.STRING)
    private YoutubeTheme theme;

    @Builder
    public Youtube(String link, String channelName, String description, String contact, YoutubeTheme theme) {
        this.link = link;
        this.channelName = channelName;
        this.description = description;
        this.contact = contact;
        this.theme = theme;
    }
}
