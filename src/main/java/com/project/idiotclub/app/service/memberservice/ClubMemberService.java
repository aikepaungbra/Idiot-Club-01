package com.project.idiotclub.app.service.memberservice;


import com.project.idiotclub.app.response.ApiResponse;
import com.project.idiotclub.app.util.member.*;

public interface ClubMemberService {

    ApiResponse joinCommunity(JoinCommunityRequestDto dto);

    ApiResponse leaveCommunity(LeaveCommunityForm form);

    ApiResponse createClub(CreateClubForm form);

    ApiResponse joinClub(JoinClubForm form);

    ApiResponse readPost(ReadPostForm form);

    ApiResponse leaveClub(LeaveClubForm form);

    ApiResponse searchCommunity(String name);

    ApiResponse viewMyCommunity(Long userId);

    ApiResponse viewProfile(Long userId);

    ApiResponse editProfile(Long userId,String photo);

    ApiResponse viewClubDetails(Long userId,Long clubId);

    ApiResponse viewCLubMembers(Long clubId);

    ApiResponse viewJoinedClub(Long userId,Long communityId);

    ApiResponse searchClub(Long communityId,String clubName);

}
