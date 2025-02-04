package com.project.idiotclub.app.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "app_user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name="user_name", updatable = false,nullable = false)
    private String name;

    @Column(name="user_email",updatable = false,nullable = false)
    private String email;

    @Column(name="user_password",updatable = false,nullable = false)
    private String password;

    @Column(name="user_profile_image")
    private String profile_image;

    @Column(name="user_role")
    @Enumerated(EnumType.STRING)
    private ClubRole role;

    @OneToMany(mappedBy = "user")
    private List<Post> posts ;

    @OneToMany(mappedBy = "user")
    private List<CommunityMembers> communityMembers;

    @OneToMany(mappedBy = "user")
    private List<JoinClubRequest> joinClubRequests;

//    @OneToMany(mappedBy = "clubLeader")
//    private List<MyClub> clubsLed;

    @OneToMany(mappedBy = "clubLeader")
    private List<CreateClubRequest> createClubRequest;

    @OneToMany(mappedBy = "user")
    private List<JoinedClubs> joinedClubs;

    @OneToMany(mappedBy = "user")
    private List<JoinCommunityRequest> joinCommunityRequest;





}
