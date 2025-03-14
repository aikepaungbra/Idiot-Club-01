package com.project.idiotclub.app.entity.creator;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.idiotclub.app.entity.*;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long communityId;
    @Column(name = "community_name", nullable = false,updatable = false)
    private String communityName;
    @Column(name = "description",nullable = false,updatable = true)
    private String description;
    @Column(name = "community_image",nullable = false,updatable = true)
    private String image;
    @Column(name = "craete_at")
    private LocalDateTime createTime;

    @OneToOne()
    @JoinColumn(name = "community_creator_id", nullable = false,unique = true)
    private CommunityCreator communityCreator;

    @OneToMany(mappedBy = "community")
    private List<CommunityMembers> communityMembers;

    @OneToMany(mappedBy = "community")
    private List<MyClub> myClubs;

    @OneToMany(mappedBy = "community")
    private List<JoinCommunityRequest> joinCommunityRequests;

    @OneToOne
    @JoinColumn(name = "community_info_id")
    @JsonManagedReference
    private CommunityInfo communityInfo;

    @OneToMany(mappedBy = "community")
    private List<CreateClubRequest> createClubRequests;



}
