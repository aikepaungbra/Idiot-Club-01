package com.project.idiotclub.app.service.creatorservice;

import com.project.idiotclub.app.response.ApiResponse;
import com.project.idiotclub.app.util.community.CheckForm;
import com.project.idiotclub.app.util.community.CommunityCreateDto;
import com.project.idiotclub.app.util.community.DecideNewClubForm;
import com.project.idiotclub.app.util.community.EditCommunityDetailsForm;

public interface CommunityCreatorService {


     ApiResponse createCommunity(CommunityCreateDto communityCreateDto);

     ApiResponse decideJoinCommunityRequest(CheckForm checkForm);

     ApiResponse decideCreateNewClubRequest(DecideNewClubForm form);

     ApiResponse editCommunityDetails(EditCommunityDetailsForm form);

     ApiResponse viewNewClubRequestDetails(Long communityId,Long createClubRequestId);

     ApiResponse viewOwnProfile(Long communityCreatorId);

     ApiResponse editProfile(Long creatorId,String photo);

     ApiResponse viewClubs(Long communityId);
     
     ApiResponse viewJoinReason(Long joinCommunityRequsetid);
     
     ApiResponse viewJoinCommunityRequest(Long communityId);
     
     ApiResponse viewAllNewClubRequest(Long communityId);
     
     ApiResponse memberCount(Long communityId);
     
    
     

}
