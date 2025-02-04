package com.project.idiotclub.app.service;

import com.project.idiotclub.app.entity.*;
import com.project.idiotclub.app.repo.*;
import com.project.idiotclub.app.response.ApiResponse;
import com.project.idiotclub.app.response.CommunityCreateResponseDto;
import com.project.idiotclub.app.util.CheckForm;
import com.project.idiotclub.app.util.CommunityCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunityCreatorServiceImpl implements CommunityCreatorService {


    private final CommunityCreatorRepo communityCreatorRepo;
    private final CommunityRepo communityRepo;
    private final CommunityInfoRepo communityInfoRepo;
    private final JoinCommunityRequestRepo joinCommunityRequestRepo;
    private final UserRepo userRepo;
    private final CommunityMembersRepo communityMembersRepo;

    @Override
    public ApiResponse createCommunity(CommunityCreateDto communityCreateDto) {

        Optional<CommunityCreator> communityCreator = communityCreatorRepo.findById(communityCreateDto.getCommunityCreatorId());

        if(communityCreator.isEmpty()){
            return new ApiResponse(false,"there is no such community creator",null);
        }
        if(communityCreator.get().getCommunity() != null){
            return new ApiResponse(false,"you are already craeted community",null);
        }


        Community community = new Community();
        community.setCommunityName(communityCreateDto.getCommunityName());
        community.setCreateTime(LocalDateTime.now());
        community.setDescription(communityCreateDto.getDescription());
        community.setImage(communityCreateDto.getImage());
        community.setCommunityCreator(communityCreator.get());

        communityRepo.save(community);

        CommunityInfo communityInfo = new CommunityInfo();
        communityInfo.setCommunity(community);
        communityInfo.setClubCount(0);
        communityInfoRepo.save(communityInfo);

        community.setCommunityInfo(communityInfo);
        var com = communityRepo.save(community);

        var responseDto = new CommunityCreateResponseDto();
        responseDto.setCommunityId(com.getCommunityId());
        responseDto.setCommunityName(com.getCommunityName());
        responseDto.setDescription(com.getDescription());
        responseDto.setImage(com.getImage());
        responseDto.setCreateAt(com.getCreateTime());
        responseDto.setCreatorName(com.getCommunityCreator().getCreatorName());
        responseDto.setCreatorEmail(com.getCommunityCreator().getCreatorEmail());
        responseDto.setCommunityInfoId(communityInfo.getId());
        responseDto.setClubCount(communityInfo.getClubCount());


        return new ApiResponse(true,"successfully created",responseDto);

    }

    @Override
    public ApiResponse checkJoinCommunityRequest(CheckForm checkForm) {

        var request = joinCommunityRequestRepo.findById(checkForm.getJoinCommunityRequestId());
        var user = userRepo.findById(checkForm.getUserId());
        var result = checkForm.getRequestStatus();
        var communityCreator = communityCreatorRepo.findById(checkForm.getCommunityCreatorId());

        if(request.isEmpty()){
            return new ApiResponse(false,"there is no such request",null);
        }
        if(user.isEmpty()){
            return new ApiResponse(false,"there is no such user",null);
        }
        if(communityCreator.isEmpty()){
            return new ApiResponse(false,"there is no such community creator",null);
        }

        if(result.equals(RequestStatus.REJECTED)){
            joinCommunityRequestRepo.deleteById(checkForm.getJoinCommunityRequestId());
            return new ApiResponse(false,"you got rejected",null);
        }

        if(result.equals(RequestStatus.PENDING)){
            return new ApiResponse(false,"you are still waiting for approval",null);
        }

        if(result.equals(RequestStatus.APPROVED)){

            var member = new CommunityMembers();
            member.setCommunity(request.get().getCommunity());
            member.setUser(user.get());
            communityMembersRepo.save(member);
            return new ApiResponse(true,"you accepted this member request",null);

        }
        return null;
    }


}
