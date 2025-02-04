package com.project.idiotclub.app.service;


import com.project.idiotclub.app.response.ApiResponse;
import com.project.idiotclub.app.util.JoinCommunityRequestDto;

public interface ClubMemberService {

    ApiResponse joinCommunity(JoinCommunityRequestDto dto);

}
