package com.tm.gogo.hikingLog;

import com.tm.gogo.domain.BaseEntity;
import com.tm.gogo.domain.hiking_trail.HikingTrail;
import com.tm.gogo.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "hiking_log")
public class HikingLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hiking_log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "hiking_date")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime hikingDate;

    @Column(name = "star_rating")
    private Integer starRating;

    @Column(name = "memo")
    private String memo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hiking_trail_id")
    private HikingTrail hikingTrail;

//    @OneToMany(mappedBy = "hiking_log", fetch = FetchType.LAZY)
//    private List<HikingLogImage> hikingLogImage = new ArrayList<>();

    @Builder
    public HikingLog(Member member, LocalDateTime hikingDate, Integer starRating, String memo, HikingTrail hikingTrail) {
        this.member = member;
        this.hikingDate = hikingDate;
        this.starRating = starRating;
        this.memo = memo;
        this.hikingTrail = hikingTrail;
    }
}
