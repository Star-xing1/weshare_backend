package com.weshare.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_info")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="info_id")
    private long infoId;

    @Column(name="email")
    private String email;

    @Column(name="username")
    private String username;

    @Column(name="name")
    private String name;

    @Column(name="phone")
    private String phone;

    @Column(name="age")
    private Integer age;

    @Column(name="job")
    private String job;

    @Column(name="path")
    private String path = "http://120.24.55.61:8080/images/defaultavatar.jpg";

    @Column(name="post_count")
    private long postCount = 0;
}
