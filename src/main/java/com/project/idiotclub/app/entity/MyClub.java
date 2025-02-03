package com.project.idiotclub.app.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class MyClub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private Long id;

    @Column(name = "club_name")
    private String name;
    @Column(name = "club_description")
    private String description;
    @Column(name = "club_logo")
    private String logo;

    @OneToMany(mappedBy = "myClub")
    private List<JoinClubRequest> joinClubRequests;

//    @ManyToOne
//    @JoinColumn(name = "club_leader_id",nullable = false)
//    private User clubLeader;

    @ManyToOne()
    @JoinColumn(name = "community_id")
    private Community community;

    @OneToMany(mappedBy = "myClub")
    private List<Post> posts;

    @OneToMany(mappedBy = "myClub")
    private List<JoinedClubs> joinedClubs;



}
