package com.project.idiotclub.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.web.multipart.MultipartFile;

@Entity
public class User {

    @Id
    @Column(name = "user_id")
    private int id;

    @Column(name="user_name", updatable = false,nullable = false)
    private String name;

    @Column(name="user_password",updatable = false,nullable = false)
    private String password;

    @Column(name="user_email",updatable = false,nullable = false)
    private String email;

    @Column(name="user_profile_image",updatable = true,nullable = true)
    private String profile_image;

    @Column(name="user_role",updatable = true,nullable = false)
    private Role role;


}
