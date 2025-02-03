package com.project.idiotclub.app.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CommunityInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "communityInfo")
    private Community community;

    @Column(name = "club_count",nullable = false)
    private int clubCount;

}
