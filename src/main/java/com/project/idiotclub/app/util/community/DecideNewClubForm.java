package com.project.idiotclub.app.util.community;

import com.project.idiotclub.app.entity.RequestStatus;
import lombok.Data;

@Data
public class DecideNewClubForm {

    private Long creatorId;
    private Long communityId;
    private Long createClubRequestId;
    private RequestStatus requestStatus;

}
