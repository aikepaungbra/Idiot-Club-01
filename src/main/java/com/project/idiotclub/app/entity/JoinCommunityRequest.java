package com.project.idiotclub.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class JoinCommunityRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String requestDescription;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "community_id",nullable = false)
    @JsonIgnore
    private Community community;

    @Column(name="reqeust_status")
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

}
