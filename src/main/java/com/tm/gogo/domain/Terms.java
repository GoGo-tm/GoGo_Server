package com.tm.gogo.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "terms")
public class Terms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "terms_id")
    private Long id;

    @Column(name = "type")
    private Type type;

    @Column(name = "required")
    private Boolean required;

    @Column(name = "title")
    private String title;

    @Column(name = "details")
    private String detail;

    //TODO:약관 타입 입력
    private enum Type {

    }
}
