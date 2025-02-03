package com.project.idiotclub.app.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class JoinCommunityRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String requestDescription;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "community_id",nullable = false)
    private Community community;

}
