package com.project.idiotclub.app.service.creatorservice;

import com.project.idiotclub.app.entity.*;
import com.project.idiotclub.app.entity.community.Community;
import com.project.idiotclub.app.entity.community.CommunityCreator;
import com.project.idiotclub.app.entity.community.CommunityInfo;
import com.project.idiotclub.app.entity.community.CommunityMembers;
import com.project.idiotclub.app.entity.community.JoinCommunityRequest;
import com.project.idiotclub.app.entity.leader.MyClub;
import com.project.idiotclub.app.entity.member.CreateClubRequest;
import com.project.idiotclub.app.entity.member.JoinClubRequest;
import com.project.idiotclub.app.entity.member.User;
import com.project.idiotclub.app.repo.community.*;
import com.project.idiotclub.app.repo.leader.MyClubRepo;
import com.project.idiotclub.app.repo.member.CreateClubRequestRepo;
import com.project.idiotclub.app.repo.member.JoinClubRequestRepo;
import com.project.idiotclub.app.repo.member.JoinedClubsRepo;
import com.project.idiotclub.app.repo.member.UserRepo;
import com.project.idiotclub.app.response.ApiResponse;
import com.project.idiotclub.app.util.community.*;
import lombok.RequiredArgsConstructor;

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
    private final JoinClubRequestRepo joinClubRequestRepo;
    

    @Override
    @Transactional
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
        communityRepo.save(community);
        
        int memberCount = communityMembersRepo.countByCommunity(community);
        		
        var responseDto = new CommunityCreateResponseDto();
        responseDto.setCommunityId(community.getCommunityId());
        responseDto.setCommunityName(community.getCommunityName());
        responseDto.setDescription(community.getDescription());
        responseDto.setImage(community.getImage());
        responseDto.setCreateAt(community.getCreateTime());
        responseDto.setCreatorName(community.getCommunityCreator().getCreatorName());
        responseDto.setCreatorEmail(community.getCommunityCreator().getCreatorEmail());
        responseDto.setCommunityInfoId(communityInfo.getId());
        responseDto.setMemberCount(memberCount);

        return new ApiResponse(true,"successfully created",responseDto);

    }

    @Override
    @Transactional
    public ApiResponse decideJoinCommunityRequest(CheckForm checkForm) {

        var request = joinCommunityRequestRepo.findById(checkForm.getJoinCommunityRequestId());
        var community = communityRepo.findById(checkForm.getCommunityId());
        var result = checkForm.getRequestStatus();
        var communityCreator = communityCreatorRepo.findById(checkForm.getCommunityCreatorId());

        if(request.isEmpty()){
            return new ApiResponse(false,"there is no such request id",null);
        }
        if(community.isEmpty()){
            return new ApiResponse(false,"there is no such community id",null);
        }

        if(communityCreator.isEmpty()){
            return new ApiResponse(false,"there is no such community creator",null);
        }
        
        JoinCommunityRequest joinRequest = request.get();
        User user = joinRequest.getUser();

        if(result.equals(RequestStatus.REJECTED)){
        	
     
        	joinRequest.setStatus(RequestStatus.REJECTED);
        	joinCommunityRequestRepo.save(joinRequest);
        	
            joinCommunityRequestRepo.deleteById(checkForm.getJoinCommunityRequestId());
            return new ApiResponse(false,"you got rejected",null);
        }

        if(result.equals(RequestStatus.PENDING)){
        	
            return new ApiResponse(false,"you are still waiting for approval",null);
        }

        if(result.equals(RequestStatus.APPROVED)){
        	
        	 if (joinRequest.getStatus() == RequestStatus.APPROVED) {
        	        return new ApiResponse(false, "This request has already been approved", null);
        	    }
        	
        	joinRequest.setStatus(RequestStatus.APPROVED);
            joinCommunityRequestRepo.save(joinRequest);

            var member = new CommunityMembers();
            member.setCommunity(request.get().getCommunity());
            member.setUser(user);
            
            boolean alreadyMember = communityMembersRepo.existsByUserAndCommunity(user, community.get());
            if (!alreadyMember) {
                communityMembersRepo.save(member);
            }
           
            
            return new ApiResponse(true,"User has been approved and added to the community",null);

        }
        return null;
    }

    @Override
    @Transactional
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
        
        if (clubRequest.getStatus() == RequestStatus.APPROVED) {
            return new ApiResponse(false, "This request has already been approved", null);
        }
        
        if (clubRequest.getStatus() == RequestStatus.REJECTED) {
            return new ApiResponse(false, "This request has already been rejected", null);
        }

        var result = form.getRequestStatus();
       

        if(result == RequestStatus.APPROVED){
        	
        	clubRequest.setStatus(RequestStatus.APPROVED);
            createClubRequestRepo.save(clubRequest);
        	
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
        	
        	createClubRequestRepo.deleteById(form.getCreateClubRequestId());        	
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
	@Transactional(readOnly = true)
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
	@Transactional(readOnly = true)
	public ApiResponse viewJoinCommunityRequest(Long communityId) {
		
		
		if(communityId == null) {
			return new ApiResponse(false, "Community id is null", null);
		}
		

	    Optional<Community> communityOpt = communityRepo.findById(communityId);
	    if (communityOpt.isEmpty()) {
	        return new ApiResponse(false, "Community not found", null);
	    }
		
		List<JoinCommunityRequest> requests = joinCommunityRequestRepo.findByCommunityAndStatus(communityOpt.get(),RequestStatus.PENDING);
		
		if(requests.isEmpty()) {
			return new ApiResponse(false, "No pending join requests found for this community", null);
		}
		
		List<ViewJoinCommunityRequestOutputForm> result = requests.stream().map(request ->{
			ViewJoinCommunityRequestOutputForm dto = new ViewJoinCommunityRequestOutputForm();
			dto.setUserId(request.getUser().getId());
			dto.setUserName(request.getUser().getName());
			dto.setUserPhoto(request.getUser().getProfile_image());
			dto.setJoinCommunityRequestId(request.getId());
			return dto;
		}).collect(Collectors.toList());	
			
		return new ApiResponse(true, "Join requests retrieved successfully", result);
	}

	@Override
	@Transactional(readOnly = true)
	public ApiResponse viewAllNewClubRequest(Long communityId) {
		
		if (communityId == null) {
	        return new ApiResponse(false, "Community ID is required", null);
	    }
		
		var community = communityRepo.findById(communityId).orElse(null);
	    if (community == null) {
	        return new ApiResponse(false, "Community not found", null);
	    }
	    
	    List<CreateClubRequest> pendingRequests = createClubRequestRepo.findByCommunityAndStatus(
	            community, RequestStatus.PENDING
	    );
	    
	    if (pendingRequests.isEmpty()) {
	        return new ApiResponse(false, "No pending club creation requests found", null);
	    }
	    
	    List<Map<String, Object>> result = pendingRequests.stream().map(req -> {
	        Map<String, Object> clubRequest = new HashMap<>();
	        clubRequest.put("requestId", req.getId());
	        clubRequest.put("clubName", req.getClubName());
	        clubRequest.put("clubLeaderName", req.getClubLeaderName());
	        clubRequest.put("description", req.getClubDescription());
	        clubRequest.put("reason", req.getReasonToCreateClub());
	        clubRequest.put("logo", req.getClubLogo());
	        return clubRequest;
	    }).collect(Collectors.toList());
		

	    return new ApiResponse(true, "Pending club creation requests retrieved successfully", result);
	}

	@Override
	@Transactional(readOnly = true)
	public ApiResponse viewMemberList(Long communityId, Long creatorId) {
		
		 if (communityId == null || creatorId == null) {
		        return new ApiResponse(false, "Community ID and Creator ID are required", null);
		 }
		 
		 var community = communityRepo.findById(communityId).orElse(null);
		 if (community == null) {
		        return new ApiResponse(false, "Community not found", null);
		 }
		 
		 if (community.getCommunityCreator().getCommunityCreatorId() != creatorId) {
		        return new ApiResponse(false, "Unauthorized: You are not the creator of this community", null);
		 }
		 
		 List<CommunityMembers> members = communityMembersRepo.findByCommunity_CommunityId(communityId);
		 
		 List<Map<String, Object>> result = members.stream().map(user -> {
		        Map<String, Object> map = new HashMap<>();
		        map.put("userId", user.getId());
		        map.put("userName", user.getUser().getName());
		        map.put("userPhoto", user.getUser().getProfile_image());
		        return map;
		 }).collect(Collectors.toList());
		
		 return new ApiResponse(true, "Community members retrieved successfully", result);
	}

	@Override
	@Transactional
	public ApiResponse removeMember(Long creatorId, Long communityId, Long userId) {
		
		if (creatorId == null || communityId == null || userId == null) {
	        return new ApiResponse(false, "Invalid request. All IDs are required.", null);
	    }
		
		var creator = communityCreatorRepo.findById(creatorId).orElse(null);
	    var community = communityRepo.findById(communityId).orElse(null);
	    var user = userRepo.findById(userId).orElse(null);
	    
	    if (creator == null) {
	        return new ApiResponse(false, "Community creator not found", null);
	    }
	    
	    if (community == null) {
	        return new ApiResponse(false, "Community not found", null);
	    }

	    if (user == null) {
	        return new ApiResponse(false, "User not found", null);
	    }
	    
	    if (!community.getCommunityCreator().equals(creator)) {
	        return new ApiResponse(false, "You are not the creator of this community", null);
	    }
	    
	    var membership = communityMembersRepo.findByUserAndCommunity(user, community);
	    if (membership == null) {
	        return new ApiResponse(false, "User is not a member of this community", null);
	    }
	    
	    communityMembersRepo.delete(membership);
	    
	    List<JoinCommunityRequest> communityRequests = joinCommunityRequestRepo.findByUserAndCommunity(user, community);
	    for (JoinCommunityRequest req : communityRequests) {
	        joinCommunityRequestRepo.delete(req);
	    }
	    
	    List<MyClub> communityClubs = myClubRepo.findByCommunity(community);
	    for (MyClub club : communityClubs) {
	        
	        var joinedClub = joinedClubsRepo.findByUserAndMyClub(user, club);
	        if (joinedClub != null) {
	            joinedClubsRepo.delete(joinedClub);
	        }

	        List<JoinClubRequest> clubRequests = joinClubRequestRepo.findByUserAndMyClub(user, club);
	        for (JoinClubRequest req : clubRequests) {
	            joinClubRequestRepo.delete(req);
	        }
	    }
		
	    return new ApiResponse(true, "User removed from community, clubs, and all related requests", null);
	}


}
