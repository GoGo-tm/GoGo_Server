package com.tm.gogo.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "mountain_image")
public class MountainImage {
    @Id @GeneratedValue
    @Column(name = "mountain_image_id")
    private Long id;

    @JoinColumn(name = "mountain_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Mountain mountain;

    @Column(name = "image_number")
    private Integer imageNumber;

    @Column(name = "image_file_name")
    private String imageFileName;


}
