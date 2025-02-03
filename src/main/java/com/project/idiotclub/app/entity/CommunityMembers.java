package com.project.idiotclub.app.entity;


import jakarta.persistence.*;

import lombok.Data;

@Entity
@Data
public class CommunityMembers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community community;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
