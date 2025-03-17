package com.project.idiotclub.app.entity.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.idiotclub.app.entity.ClubRole;
import com.project.idiotclub.app.entity.community.CommunityMembers;
import com.project.idiotclub.app.entity.community.JoinCommunityRequest;
import com.project.idiotclub.app.entity.leader.Post;
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
    @JsonIgnore
    private List<Post> posts ;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<CommunityMembers> communityMembers;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<JoinClubRequest> joinClubRequests;

//    @OneToMany(mappedBy = "clubLeader")
//    private List<MyClub> clubsLed;

    @OneToMany(mappedBy = "clubLeader")
    @JsonIgnore
    private List<CreateClubRequest> createClubRequest;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<JoinedClubs> joinedClubs;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<JoinCommunityRequest> joinCommunityRequest;


}
