package com.project.idiotclub.app.entity.community;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.idiotclub.app.entity.member.User;
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
    @JsonIgnore
    private Community community;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
