package com.project.idiotclub.app.service.memberservice;

import com.project.idiotclub.app.entity.*;
import com.project.idiotclub.app.entity.community.JoinCommunityRequest;
import com.project.idiotclub.app.entity.member.CreateClubRequest;
import com.project.idiotclub.app.entity.member.JoinClubRequest;
import com.project.idiotclub.app.repo.community.CommunityMembersRepo;
import com.project.idiotclub.app.repo.community.CommunityRepo;
import com.project.idiotclub.app.repo.community.JoinCommunityRequestRepo;
import com.project.idiotclub.app.repo.leader.MyClubRepo;
import com.project.idiotclub.app.repo.member.CreateClubRequestRepo;
import com.project.idiotclub.app.repo.member.JoinClubRequestRepo;
import com.project.idiotclub.app.repo.member.UserRepo;
import com.project.idiotclub.app.response.ApiResponse;
import com.project.idiotclub.app.util.member.CreateClubForm;
import com.project.idiotclub.app.util.member.JoinClubForm;
import com.project.idiotclub.app.util.member.JoinCommunityRequestDto;
import com.project.idiotclub.app.util.member.LeaveCommunityForm;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClubMemberServiceImpl implements ClubMemberService {


    private final UserRepo userRepo;
    private final CommunityRepo communityRepo;
    private final JoinCommunityRequestRepo joinCommunityRequestRepo;
    private final CommunityMembersRepo communityMembersRepo;
    private final MyClubRepo myClubRepo;
    private final CreateClubRequestRepo createClubRequestRepo;
    private final JoinClubRequestRepo joinClubRequestRepo;

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

    @Override
    public ApiResponse leaveCommunity(LeaveCommunityForm form) {

        var community = communityRepo.findById(form.getCommunityId()).orElse(null);
        var user = userRepo.findById(form.getUserId()).orElse(null);



        if(community == null){
            return new ApiResponse(false,"community is empty",null);
        }

        if(user == null){
            return new ApiResponse(false,"user is empty",null);
        }

        boolean isMember = communityMembersRepo.existsByUserAndCommunity(user,community);

        if(!isMember){
            return new ApiResponse(false,"you are not member of this community",null);
        }

        communityMembersRepo.deleteByUserAndCommunity(user,community);

        return new ApiResponse(true,"successfully leaving this community",null);
    }

    @Override
    @Transactional
    public ApiResponse createClub(CreateClubForm form) {

        var community = communityRepo.findById(form.getCommunityId()).orElse(null);
        var user = userRepo.findById(form.getUserId()).orElse(null);

        if(community == null){
            return new ApiResponse(false,"community is empty",null);
        }

        if(user == null){
            return new ApiResponse(false,"user is empty",null);
        }

        var isMember = communityMembersRepo.existsByUserAndCommunity(user,community);

        if(!isMember){
            return new ApiResponse(false,"you are not member of this community",null);
        }
        var communityCreator = community.getCommunityCreator();
        if(communityCreator == null){
            return new ApiResponse(false,"This community has no creator assigned",null);
        }

        var clubRequest = new CreateClubRequest();

        clubRequest.setClubName(form.getClubName());
        clubRequest.setClubDescription(form.getClubDescription());
        clubRequest.setClubLogo(form.getClubLogo());
        clubRequest.setReasonToCreateClub(form.getReasonToCreateClub());
        clubRequest.setClubLeaderName(user.getName());
        clubRequest.setClubLeader(user);
        clubRequest.setCommunityCreator(communityCreator);
        clubRequest.setCommunity(community);
        clubRequest.setStatus(RequestStatus.PENDING);
        createClubRequestRepo.save(clubRequest);

        return new ApiResponse(true,"Club creation request submitted successfully",null);
    }

    @Override
    public ApiResponse joinClub(JoinClubForm form) {

        var community = communityRepo.findById(form.getCommunityId()).orElse(null);
        if(community == null){
            return new ApiResponse(false,"community is empty",null);
        }

        var club = myClubRepo.findById(form.getClubId()).orElse(null);
        if(club == null){
            return new ApiResponse(false,"club is empty",null);
        }

        var user = userRepo.findById(form.getUserId()).orElse(null);
        if(user == null){
            return new ApiResponse(false,"user is empty",null);
        }

        var isMember = communityMembersRepo.existsByUserAndCommunity(user,community);

        if(!isMember){
            return new ApiResponse(false,"you are not member of this community",null);
        }

        var joinClubRequest = new JoinClubRequest();
        joinClubRequest.setUser(user);
        joinClubRequest.setMyClub(club);
        joinClubRequest.setRequestStatus(RequestStatus.PENDING);
        joinClubRequestRepo.save(joinClubRequest);

        return new ApiResponse(true,"successfully requested join club",null);
    }


}
