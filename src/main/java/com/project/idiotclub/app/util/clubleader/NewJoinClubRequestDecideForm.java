package com.project.idiotclub.app.util.clubleader;

import com.project.idiotclub.app.entity.RequestStatus;
import lombok.Data;

@Data
public class NewJoinClubRequestDecideForm {

    private Long communityId;
    private Long clubLeaderId;
    private Long clubId;
    private Long joinClubRequestId;
    private RequestStatus requestStatus;

}
