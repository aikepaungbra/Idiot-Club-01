package com.project.idiotclub.app.service.memberservice;


import com.project.idiotclub.app.response.ApiResponse;
import com.project.idiotclub.app.util.member.CreateClubForm;
import com.project.idiotclub.app.util.member.JoinCommunityRequestDto;
import com.project.idiotclub.app.util.member.LeaveCommunityForm;

public interface ClubMemberService {

    ApiResponse joinCommunity(JoinCommunityRequestDto dto);

    ApiResponse leaveCommunity(LeaveCommunityForm form);

    ApiResponse createClub(CreateClubForm form);

}
