package com.project.idiotclub.app.service;

import com.project.idiotclub.app.entity.JoinCommunityRequest;
import com.project.idiotclub.app.entity.RequestStatus;
import com.project.idiotclub.app.repo.CommunityMembersRepo;
import com.project.idiotclub.app.repo.CommunityRepo;
import com.project.idiotclub.app.repo.JoinCommunityRequestRepo;
import com.project.idiotclub.app.repo.UserRepo;
import com.project.idiotclub.app.response.ApiResponse;
import com.project.idiotclub.app.util.JoinCommunityRequestDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClubMemberServiceImpl implements ClubMemberService {


    private final UserRepo userRepo;
    private final CommunityRepo communityRepo;
    private final JoinCommunityRequestRepo joinCommunityRequestRepo;
    private final CommunityMembersRepo communityMembersRepo;

    @Override
    public ApiResponse joinCommunity(JoinCommunityRequestDto dto) {

        var user = userRepo.findById(dto.getUserId());
        var community = communityRepo.findById(dto.getCommunityId());

        if(user.isEmpty()){
            return new ApiResponse(false,"user is empty",null);
        }
        if(community.isEmpty()){
            return new ApiResponse(false,"there is no community with this community id",null);
        }

        boolean isAlreadyRequest = joinCommunityRequestRepo.existsByUserAndCommunity(user.get(),community.get());
        if(isAlreadyRequest){
            return new ApiResponse(false,"you are already request for this community",null);
        }

        boolean isAlreadyMember = communityMembersRepo.existsByUserAndCommunity(user.get(),community.get());
        if(isAlreadyMember){
            return new ApiResponse(false,"you are already member of this community",null);
        }

        var joinCommunityrequest = new JoinCommunityRequest();
        joinCommunityrequest.setRequestDescription(dto.getRequestDescription());
        joinCommunityrequest.setCommunity(community.get());
        joinCommunityrequest.setUser(user.get());
        joinCommunityrequest.setStatus(RequestStatus.PENDING);
        joinCommunityRequestRepo.save(joinCommunityrequest);

        return new ApiResponse(true,"success",joinCommunityrequest);
    }
}
