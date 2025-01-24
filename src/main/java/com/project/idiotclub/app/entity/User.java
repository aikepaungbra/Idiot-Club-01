package com.project.idiotclub.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name="user_name", updatable = false,nullable = false)
    private String name;

    @Column(name="user_password",updatable = false,nullable = false)
    private String password;

    @Column(name="user_email",updatable = false,nullable = false)
    private String email;

    @Column(name="user_profile_image",updatable = true,nullable = true)
    private String profile_image;

    @Column(name="user_role",updatable = true,nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;


}
