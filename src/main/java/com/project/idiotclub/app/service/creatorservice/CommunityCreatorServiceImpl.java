package com.project.idiotclub.app.service.creatorservice;

import com.project.idiotclub.app.entity.*;
import com.project.idiotclub.app.entity.community.Community;
import com.project.idiotclub.app.entity.community.CommunityCreator;
import com.project.idiotclub.app.entity.community.CommunityInfo;
import com.project.idiotclub.app.entity.community.CommunityMembers;
import com.project.idiotclub.app.entity.community.JoinCommunityRequest;
import com.project.idiotclub.app.entity.leader.MyClub;
import com.project.idiotclub.app.repo.community.*;
import com.project.idiotclub.app.repo.leader.MyClubRepo;
import com.project.idiotclub.app.repo.member.CreateClubRequestRepo;
import com.project.idiotclub.app.repo.member.JoinedClubsRepo;
import com.project.idiotclub.app.repo.member.UserRepo;
import com.project.idiotclub.app.response.ApiResponse;
import com.project.idiotclub.app.util.community.*;
import lombok.RequiredArgsConstructor;
import lombok.experimental.var;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final JoinedClubsRepo joinedClubsRepo;

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
        	
        	JoinCommunityRequest joinRequest = request.get();   
        	joinRequest.setStatus(RequestStatus.REJECTED);
        	joinCommunityRequestRepo.save(joinRequest);
        	
            joinCommunityRequestRepo.deleteById(checkForm.getJoinCommunityRequestId());
            return new ApiResponse(false,"you got rejected",null);
        }

        if(result.equals(RequestStatus.PENDING)){
        	
            return new ApiResponse(false,"you are still waiting for approval",null);
        }

        if(result.equals(RequestStatus.APPROVED)){
        	
        	JoinCommunityRequest joinRequest = request.get();
        	
        	joinRequest.setStatus(RequestStatus.APPROVED);
            joinCommunityRequestRepo.save(joinRequest);

            var member = new CommunityMembers();
            member.setCommunity(request.get().getCommunity());
            member.setUser(user.get());
            communityMembersRepo.save(member);
            
            joinCommunityRequestRepo.deleteById(checkForm.getJoinCommunityRequestId());
            
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
            return new ApiResponse(false, "Club request rejected", null);
        };
        if(result == RequestStatus.PENDING){
            return new ApiResponse(false, "Club request pending", null);
        }

        return new ApiResponse(false, "Invalid request status", null);
    }

    @Override
    @Transactional
    public ApiResponse editCommunityDetails(EditCommunityDetailsForm form) {

        if (form.getCommunityId() == null || form.getLeaderId() == null ||
                form.getNewCommunityName() == null || form.getNewCommunityDescription() == null || form.getNewCommunityLogo() == null) {
            return new ApiResponse(false, "Invalid request. All fields are required.", null);
        }

        var community = communityRepo.findById(form.getCommunityId()).orElse(null);
        if (community == null) {
            return new ApiResponse(false, "Community not found", null);
        }

        var leader = userRepo.findById(form.getLeaderId()).orElse(null);
        if (leader == null) {
            return new ApiResponse(false, "Community creator not found", null);
        }

        if (community.getCommunityCreator().getCommunityCreatorId()!=(leader.getId())) {
            return new ApiResponse(false, "Only the community creator can edit this community", null);
        }

        community.setCommunityName(form.getNewCommunityName());
        community.setDescription(form.getNewCommunityDescription());
        community.setImage(form.getNewCommunityLogo());
        communityRepo.save(community);

        return new ApiResponse(true, "Community details updated successfully", community);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse viewNewClubRequestDetails(Long communityId, Long createClubRequestId) {

        if (communityId == null || createClubRequestId == null) {
            return new ApiResponse(false, "Invalid request. Community ID and Request ID are required.", null);
        }

        var community = communityRepo.findById(communityId).orElse(null);
        if (community == null) {
            return new ApiResponse(false, "Community not found", null);
        }

        var createClubRequest = createClubRequestRepo.findById(createClubRequestId).orElse(null);
        if (createClubRequest == null) {
            return new ApiResponse(false, "Create club request not found", null);
        }

        if (!createClubRequest.getCommunity().equals(community)) {
            return new ApiResponse(false, "This request does not belong to the specified community", null);
        }

        Map<String, Object> clubRequestDetails = new HashMap<>();
        clubRequestDetails.put("clubName", createClubRequest.getClubName());
        clubRequestDetails.put("clubDescription", createClubRequest.getClubDescription());
        clubRequestDetails.put("clubLeaderName", createClubRequest.getClubLeaderName());
        clubRequestDetails.put("reasonToCreateClub", createClubRequest.getReasonToCreateClub());
        clubRequestDetails.put("clubLogo", createClubRequest.getClubLogo());
        clubRequestDetails.put("status", createClubRequest.getStatus().toString());


        return new ApiResponse(true, "Create club request details retrieved successfully", clubRequestDetails);

    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse viewOwnProfile(Long communityCreatorId) {

        if(communityCreatorId == null){
            return new ApiResponse(false, "Invalid request. Community Creator ID is required.", null);
        }

        var communityCreator = communityCreatorRepo.findById(communityCreatorId).orElse(null);
        if (communityCreator == null) {
            return new ApiResponse(false, "Community Creator not found", null);
        }

        Map<String, Object> profileDetails = new HashMap<>();
        profileDetails.put("creatorId", communityCreator.getCommunityCreatorId());
        profileDetails.put("creatorName", communityCreator.getCreatorName());
        profileDetails.put("creatorEmail", communityCreator.getCreatorEmail());
        profileDetails.put("photo",communityCreator.getCreatorPhoto());

        return new ApiResponse(true, "Community Creator profile retrieved successfully", profileDetails);
    }

    @Override
    @Transactional
    public ApiResponse editProfile(Long creatorId, String photo) {

        if (creatorId == null || photo == null || photo.trim().isEmpty()) {
            return new ApiResponse(false, "Invalid request. Creator ID and photo are required.", null);
        }

        var communityCreator = communityCreatorRepo.findById(creatorId).orElse(null);
        if (communityCreator == null) {
            return new ApiResponse(false, "Community Creator not found", null);
        }
        communityCreator.setCreatorPhoto(photo);
        communityCreatorRepo.save(communityCreator);
        return new ApiResponse(true, "Profile photo updated successfully", communityCreator);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse viewClubs(Long communityId) {

        if (communityId == null) {
            return new ApiResponse(false, "Invalid request. Community ID is required.", null);
        }

        var community = communityRepo.findById(communityId).orElse(null);
        if (community == null) {
            return new ApiResponse(false, "Community not found", null);
        }

        List<Map<String, Object>> clubs = myClubRepo.findByCommunity(community)
                .stream()
                .map(club -> {
                    Map<String, Object> clubInfo = new HashMap<>();
                    clubInfo.put("clubId", club.getId());
                    clubInfo.put("clubName", club.getName());
                    clubInfo.put("clubDescription", club.getDescription());
                    clubInfo.put("clubLogo", club.getLogo());

                    int totalMembers = joinedClubsRepo.countByMyClub(club);
                    clubInfo.put("totalMembers", totalMembers);

                    return clubInfo;
                })
                .collect(Collectors.toList());

        if (clubs.isEmpty()) {
            return new ApiResponse(false, "No clubs available in this community", null);
        }

        return new ApiResponse(true, "Clubs retrieved successfully", clubs);

    }

	@Override
	public ApiResponse viewJoinReason(Long joinCommunityRequsetid) {
		
		if(joinCommunityRequsetid == null) {
			return new ApiResponse(false, "Request id is null", null);
		}
		
		var request = joinCommunityRequestRepo.findById(joinCommunityRequsetid).orElse(null);
		if(request == null) {
			return new ApiResponse(false, "there is no request for this id", joinCommunityRequsetid);
		}
		
		return new ApiResponse(true, "Data retrieved successfully", request.getRequestDescription());
	}

	@Override
	public ApiResponse viewJoinCommunityRequest( Long communityId) {
		
		
		if(communityId == null) {
			return new ApiResponse(false, "Community id is null", null);
		}
		

	    Optional<Community> communityOpt = communityRepo.findById(communityId);
	    if (communityOpt.isEmpty()) {
	        return new ApiResponse(false, "Community not found", null);
	    }
		
		List<JoinCommunityRequest> requests = joinCommunityRequestRepo.findByCommunity(communityOpt.get());
		
		if(requests.isEmpty()) {
			return new ApiResponse(false, "No join requests found for this community", null);
		}
		
		List<ViewJoinCommunityRequestOutputForm> result = requests.stream().map(request ->{
			ViewJoinCommunityRequestOutputForm dto = new ViewJoinCommunityRequestOutputForm();
			dto.setUserId(request.getUser().getId());
			dto.setUserName(request.getUser().getName());
			dto.setUserPhoto(request.getUser().getProfile_image());
			return dto;
		}).collect(Collectors.toList());	
			
		return new ApiResponse(true, "Join requests retrieved successfully", result);
	}


}
