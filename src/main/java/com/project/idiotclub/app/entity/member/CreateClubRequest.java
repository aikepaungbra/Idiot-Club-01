package com.project.idiotclub.app.entity.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.idiotclub.app.entity.RequestStatus;
import com.project.idiotclub.app.entity.community.Community;
import com.project.idiotclub.app.entity.community.CommunityCreator;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CreateClubRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clubName;
    private String clubDescription;
    private String clubLeaderName;
    private String reasonToCreateClub;
    private String clubLogo;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @ManyToOne
    @JoinColumn(name = "club_lader_id")
    @JsonIgnore
    private User clubLeader;

    @ManyToOne
    @JoinColumn(name = "community_creator_id",nullable = false)
    @JsonIgnore
    private CommunityCreator communityCreator;

    @ManyToOne
    @JoinColumn(name = "community_id",nullable = false)
    @JsonIgnore
    private Community community;


}
