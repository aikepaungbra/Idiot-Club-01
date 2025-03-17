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

}
