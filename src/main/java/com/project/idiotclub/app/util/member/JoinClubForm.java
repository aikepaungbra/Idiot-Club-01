package com.project.idiotclub.app.util.member;

import lombok.Data;

@Data
public class JoinClubForm {

    private Long clubId;
    private Long communityId;
    private Long userId;
    private String reasonToJoinThisClub;

}
