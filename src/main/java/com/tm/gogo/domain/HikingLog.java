package com.tm.gogo.domain;

import com.tm.gogo.domain.member.Member;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
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

    @Column(name = "log_image1")
    private String logImage1;

    @Column(name = "log_image2")
    private String logImage2;

    @Column(name = "log_image3")
    private String logImage3;

    @Column(name = "log_image4")
    private String logImage4;

    @Column(name = "log_image5")
    private String logImage5;


}
