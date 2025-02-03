package com.project.idiotclub.app.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CreateClubRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clubName;
    private String aboutTheClub;
    private String clubLeaderName;
    private String reasonToCreateClub;
    private String clubPhoto;

    private RequestStatus status;

    @ManyToOne
    @JoinColumn(name = "club_lader_id")
    private User clubLeader;

    @ManyToOne
    @JoinColumn(name = "community_creator_id",nullable = false)
    private CommunityCreator communityCreator;

    @ManyToOne
    @JoinColumn(name = "community_id",nullable = false)
    private Community community;


}
