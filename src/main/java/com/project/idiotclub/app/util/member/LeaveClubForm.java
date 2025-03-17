package com.project.idiotclub.app.util.member;


import lombok.Data;

@Data
public class LeaveClubForm {

    private Long communityId;
    private Long clubId;
    private Long userId;

}
