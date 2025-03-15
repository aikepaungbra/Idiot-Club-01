package com.project.idiotclub.app.service.creatorservice;

import com.project.idiotclub.app.entity.*;
import com.project.idiotclub.app.entity.creator.Community;
import com.project.idiotclub.app.entity.creator.CommunityCreator;
import com.project.idiotclub.app.entity.creator.CommunityInfo;
import com.project.idiotclub.app.repo.*;
import com.project.idiotclub.app.response.ApiResponse;
import com.project.idiotclub.app.util.community.CommunityCreateResponseDto;
import com.project.idiotclub.app.util.community.CheckForm;
import com.project.idiotclub.app.util.community.CommunityCreateDto;
import com.project.idiotclub.app.util.community.DecideNewClubForm;
import lombok.RequiredArgsConstructor;
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
    private final CreateClubRequestRepo createClubRequestRepo;
    private final MyClubRepo myClubRepo;

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
    public ApiResponse decideJoinCommunityRequest(CheckForm checkForm) {

        var request = joinCommunityRequestRepo.findById(checkForm.getJoinCommunityRequestId());
        var community = communityRepo.findById(checkForm.getCommunityId());
        var user = userRepo.findById(checkForm.getUserId());
        var result = checkForm.getRequestStatus();
        var communityCreator = communityCreatorRepo.findById(checkForm.getCommunityCreatorId());

        if(request.isEmpty()){
            return new ApiResponse(false,"there is no such request id",null);
        }
        if(community.isEmpty()){
            return new ApiResponse(false,"there is no such community id",null);
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

    @Override
    public ApiResponse decideCreateNewClubRequest(DecideNewClubForm form) {

        var communityCreator = communityCreatorRepo.findById(form.getCreatorId()).orElse(null);
        if (communityCreator == null) {
            return new ApiResponse(false, "Invalid Community Creator ID", null);
        }

        var community = communityRepo.findById(form.getCommunityId()).orElse(null);
        if (community == null) {
            return new ApiResponse(false, "Invalid Community ID", null);
        }

        var clubRequest = createClubRequestRepo.findById(form.getCreateClubRequestId()).orElse(null);
        if (clubRequest == null) {
            return new ApiResponse(false, "Club creation request not found", null);
        }

        if (!clubRequest.getCommunityCreator().equals(communityCreator) || !clubRequest.getCommunity().equals(community)) {
            return new ApiResponse(false, "This request does not belong to the given community or creator", null);
        }

        var result = form.getRequestStatus();
        clubRequest.setStatus(result);
        createClubRequestRepo.save(clubRequest);

        if(result == RequestStatus.APPROVED){
            var newCLub = new MyClub();
            newCLub.setName(clubRequest.getClubName());
            newCLub.setDescription(clubRequest.getClubDescription());
            newCLub.setLogo(clubRequest.getClubLogo());
            newCLub.setCommunity(community);
            newCLub.setClubLeader(clubRequest.getClubLeader());

            myClubRepo.save(newCLub);

            var communityInfo = community.getCommunityInfo();
            if(communityInfo == null){
                communityInfo = new CommunityInfo();
                communityInfo.setCommunity(community);
                communityInfo.setClubCount(1);
            }
            else {
                communityInfo.setClubCount(communityInfo.getClubCount() + 1);
            }
            communityInfoRepo.save(communityInfo);

            return new ApiResponse(true, "Club request approved and club created successfully", null);

        }

        if(result == RequestStatus.REJECTED){
            return new ApiResponse(true, "Club request rejected", null);
        };
        if(result == RequestStatus.PENDING){
            return new ApiResponse(true, "Club request pending", null);
        }

        return new ApiResponse(true, "Invalid request status", null);
    }


}
