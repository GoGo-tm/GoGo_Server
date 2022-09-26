package com.tm.gogo.domain.hiking_log;

import com.tm.gogo.domain.BaseEntity;
import com.tm.gogo.domain.hiking_trail.HikingTrail;
import com.tm.gogo.domain.member.Member;
import com.tm.gogo.web.hiking_log.HikingLogRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime hikingDate;

    @Column(name = "star_rating")
    private Integer starRating;

    @Column(name = "memo")
    private String memo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hiking_trail_id")
    private HikingTrail hikingTrail;

    @OneToMany(mappedBy = "hikingLog", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<HikingLogImage> hikingLogImages = new ArrayList<>();

    @Builder
    public HikingLog(Member member, LocalDateTime hikingDate, Integer starRating, String memo, HikingTrail hikingTrail, List<String> imageUrls) {
        this.member = member;
        this.hikingDate = hikingDate;
        this.starRating = starRating;
        this.memo = memo;
        this.hikingTrail = hikingTrail;
        initHikingLogImages(imageUrls);
    }

    public void initHikingLogImages(List<String> imageUrls) {
        if (CollectionUtils.isEmpty(imageUrls)) return;

        for (int i = 0; i < imageUrls.size(); i++) {
            String imageUrl = imageUrls.get(i);
            HikingLogImage hikingLogImage = HikingLogImage.builder()
                    .url(imageUrl)
                    .hikingLog(this)
                    .build();

            hikingLogImages.add(hikingLogImage);
        }
    }

    public List<String> getHikingLogImageUrls() {
        return hikingLogImages.stream()
                .map(HikingLogImage::getUrl)
                .collect(Collectors.toList());
    }

    public void update(HikingLogRequest hikingLogRequest, HikingTrail hikingTrail) {
        this.hikingDate = hikingLogRequest.getHikingDate();
        this.starRating = hikingLogRequest.getStarRating();
        this.memo = hikingLogRequest.getMemo();
        this.hikingTrail = hikingTrail;
        hikingLogImages.clear();
        initHikingLogImages(hikingLogRequest.getImageUrls());
    }
}
