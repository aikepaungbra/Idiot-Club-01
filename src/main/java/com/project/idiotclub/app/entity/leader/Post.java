package com.project.idiotclub.app.entity.leader;

import com.project.idiotclub.app.entity.member.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String message;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "club_id",nullable = false)
    private MyClub myClub;

    private LocalDateTime createdAt;

}
