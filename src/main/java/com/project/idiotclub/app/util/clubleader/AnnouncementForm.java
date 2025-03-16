package com.project.idiotclub.app.util.clubleader;

import lombok.Data;

@Data
public class AnnouncementForm {

    private Long leaderId;
    private Long clubId;
    private Long communityId;
    private String message;

}
