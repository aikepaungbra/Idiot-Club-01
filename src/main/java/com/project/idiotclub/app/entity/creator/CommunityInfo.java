package com.project.idiotclub.app.entity.creator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CommunityInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "communityInfo")
    @JsonIgnore
    private Community community;

    @Column(name = "club_count")
    private int clubCount;

}
