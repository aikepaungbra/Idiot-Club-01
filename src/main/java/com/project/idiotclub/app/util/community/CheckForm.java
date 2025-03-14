package com.project.idiotclub.app.util.community;

import com.project.idiotclub.app.entity.RequestStatus;
import lombok.Data;

@Data
public class CheckForm {

    private RequestStatus requestStatus;
    private Long joinCommunityRequestId;
    private Long userId;
    private Long communityCreatorId;
    private Long communityId;

}
