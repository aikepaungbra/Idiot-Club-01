package com.project.idiotclub.app.entity.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.idiotclub.app.entity.ClubRole;
import com.project.idiotclub.app.entity.leader.MyClub;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class JoinedClubs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "club_id",nullable = false)
    @JsonIgnore
    private MyClub myClub;

    @Column(name = "membership_role",nullable = false)
    private ClubRole role;

}
