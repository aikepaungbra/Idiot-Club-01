package com.project.idiotclub.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class Poster {

    @Id
    @Column(name = "user_id")
    private int id;

    @Column(name="poster_description", updatable = false,nullable = false)
    private String description;

    @Column(name="poster_image")
    private String image;



}
