package com.project.idiotclub.app.entity.community;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.idiotclub.app.entity.member.CreateClubRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "community_creator")
@AllArgsConstructor
@NoArgsConstructor
public class CommunityCreator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long communityCreatorId;
    @Column(name = "creator_name",nullable = false)
    private String creatorName;
    @Column(name = "creator_email", unique = true)
    private String creatorEmail;
    @Column(name = "creator_password", nullable = false)
    private String creatorPassword;

    @Lob
    private String creatorPhoto;

    @OneToOne(mappedBy = "communityCreator")
    @JsonBackReference
    private Community community;

    @OneToMany(mappedBy = "communityCreator")
    @JsonIgnore
    private List<CreateClubRequest> createClubRequests;


}
