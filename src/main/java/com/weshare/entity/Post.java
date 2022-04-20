package com.weshare.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="post_id")
    private long postid;

    @Column(name="description")
    private String description;

    @Column(name="img_url")
    private String img_url;

    @Column(name="created_at")
    private Date createdAt;

    @Column(name="info_id")
    private long infoId;

    @Column(name="likes")
    private Long likes;
}
