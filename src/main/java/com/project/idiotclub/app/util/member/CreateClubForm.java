package com.project.idiotclub.app.util.member;

import lombok.Data;

@Data
public class CreateClubForm {

    private Long userId;
    private Long communityId;
    private String clubName;
    private String clubDescription;
    private String clubLogo;
    private String reasonToCreateClub;


}
