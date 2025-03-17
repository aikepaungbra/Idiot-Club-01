package com.project.idiotclub.app.entity.leader;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.idiotclub.app.entity.member.JoinClubRequest;
import com.project.idiotclub.app.entity.member.User;
import com.project.idiotclub.app.entity.community.Community;
import com.project.idiotclub.app.entity.member.JoinedClubs;
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
    @JsonIgnore
    private List<JoinClubRequest> joinClubRequests;

    @ManyToOne()
    @JoinColumn(name = "community_id")
    @JsonIgnore
    private Community community;

    @OneToMany(mappedBy = "myClub")
    @JsonIgnore
    private List<Post> posts;

    @OneToMany(mappedBy = "myClub")
    @JsonIgnore
    private List<JoinedClubs> joinedClubs;

    @ManyToOne
    @JoinColumn(name = "club_leader_id", nullable = false)
    @JsonIgnore
    private User clubLeader;

}
