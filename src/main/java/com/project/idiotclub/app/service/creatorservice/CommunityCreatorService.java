package com.project.idiotclub.app.service.creatorservice;

import com.project.idiotclub.app.response.ApiResponse;
import com.project.idiotclub.app.util.community.CheckForm;
import com.project.idiotclub.app.util.community.CommunityCreateDto;
import com.project.idiotclub.app.util.community.DecideNewClubForm;

public interface CommunityCreatorService {


     ApiResponse createCommunity(CommunityCreateDto communityCreateDto);

     ApiResponse decideJoinCommunityRequest(CheckForm checkForm);

     ApiResponse decideCreateNewClubRequest(DecideNewClubForm form);

}
