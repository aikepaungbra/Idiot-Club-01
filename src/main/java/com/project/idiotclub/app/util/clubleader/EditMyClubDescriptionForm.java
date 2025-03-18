package com.project.idiotclub.app.util.clubleader;

import lombok.Data;

@Data
public class EditMyClubDescriptionForm {

    private Long leaderId;
    private Long clubId;
    private String newDescription;

}
