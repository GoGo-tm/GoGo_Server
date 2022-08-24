package com.tm.gogo.domain.favorite_trail;

import com.tm.gogo.domain.BaseEntity;
import com.tm.gogo.domain.hiking_trail.HikingTrail;
import com.tm.gogo.domain.member.Member;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "favorite_trail")
public class FavoriteTrail extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_trail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hiking_trail_id")
    private HikingTrail hikingTrail;

    @Builder
    public FavoriteTrail(Member member, HikingTrail hikingTrail) {
        this.member = member;
        this.hikingTrail = hikingTrail;
    }
}
