package com.project.idiotclub.app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Club {

    @Id
    private int id;
    private String name;
    private String description;
    private String clubLogo;
    private String post;


}
