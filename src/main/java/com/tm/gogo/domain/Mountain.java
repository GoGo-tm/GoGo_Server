package com.tm.gogo.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "mountain")
public class Mountain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mountain_id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "top")
    private String top;

    @Column(name = "sub_name")
    private String subName;

    @Column(name = "address")
    private String address;

    @Column(name = "height")
    private String height;

    @Column(name = "administrator")
    private String administrator;

    @Column(name = "administrator_phone_number")
    private String administratorPhoneNumber;

    @Column(name = "summary")
    private String summary;

    @Column(name = "detail")
    private String detail;
}
