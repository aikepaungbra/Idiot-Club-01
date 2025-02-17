package com.project.idiotclub.app.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class JoinClubRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    private MyClub myClub;

    private RequestStatus requestStatus;


}


