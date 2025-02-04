package com.project.idiotclub.app.service;

import com.project.idiotclub.app.response.ApiResponse;
import com.project.idiotclub.app.util.CheckForm;
import com.project.idiotclub.app.util.CommunityCreateDto;

public interface CommunityCreatorService {


     ApiResponse createCommunity(CommunityCreateDto communityCreateDto);

     ApiResponse checkJoinCommunityRequest(CheckForm checkForm);

}
