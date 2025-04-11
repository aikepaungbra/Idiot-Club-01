package com.project.idiotclub.app.service.memberservice;

import com.project.idiotclub.app.entity.*;
import com.project.idiotclub.app.entity.community.JoinCommunityRequest;
import com.project.idiotclub.app.entity.leader.MyClub;
import com.project.idiotclub.app.entity.leader.Post;
import com.project.idiotclub.app.entity.member.CreateClubRequest;
import com.project.idiotclub.app.entity.member.JoinClubRequest;
import com.project.idiotclub.app.repo.community.CommunityMembersRepo;
import com.project.idiotclub.app.repo.community.CommunityRepo;
import com.project.idiotclub.app.repo.community.JoinCommunityRequestRepo;
import com.project.idiotclub.app.repo.leader.MyClubRepo;
import com.project.idiotclub.app.repo.leader.PostRepo;
import com.project.idiotclub.app.repo.member.CreateClubRequestRepo;
import com.project.idiotclub.app.repo.member.JoinClubRequestRepo;
import com.project.idiotclub.app.repo.member.JoinedClubsRepo;
import com.project.idiotclub.app.repo.member.UserRepo;
import com.project.idiotclub.app.response.ApiResponse;
import com.project.idiotclub.app.util.member.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final PostRepo postRepo;
    private final JoinedClubsRepo joinedClubsRepo;

    @Override
    @Transactional
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
    @Transactional
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
    @Transactional
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
        joinClubRequest.setReasonToJoin(form.getReasonToJoinThisClub());
        joinClubRequest.setRequestStatus(RequestStatus.PENDING);
        joinClubRequestRepo.save(joinClubRequest);

        return new ApiResponse(true,"successfully requested join club",null);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse readPost(ReadPostForm form) {

        if(form.getClubId() == null || form.getClubId() == null){
            return new ApiResponse(false,"Invalid request. Club ID or Community ID is missing.",null);
        }

        var club = myClubRepo.findById(form.getClubId()).orElse(null);
        var community = communityRepo.findById(form.getCommunityId()).orElse(null);

        if(community == null){
            return new ApiResponse(false,"community not found",null);
        }
        if(club == null){
            return new ApiResponse(false,"club not found",null);
        }
        if(!club.getCommunity().equals(community)){
            return new ApiResponse(false, "This club does not belong to the given community", null);
        }

        List<Post> posts = postRepo.findByMyClubOrderByCreatedAtDesc(club);

        return new ApiResponse(true,"Posts retrieved successfully",posts);
    }

    @Override
    @Transactional
    public ApiResponse leaveClub(LeaveClubForm form) {

        if (form.getCommunityId() == null || form.getClubId() == null || form.getUserId() == null) {
            return new ApiResponse(false, "Invalid request. One or more required fields are missing.", null);
        }

        var community = communityRepo.findById(form.getCommunityId()).orElse(null);
        var club = myClubRepo.findById(form.getClubId()).orElse(null);
        var user = userRepo.findById(form.getUserId()).orElse(null);

        if (community == null) {
            return new ApiResponse(false, "Community not found", null);
        }
        if (club == null) {
            return new ApiResponse(false, "Club not found", null);
        }
        if (user == null) {
            return new ApiResponse(false, "User not found", null);
        }

        if (!club.getCommunity().equals(community)) {
            return new ApiResponse(false, "This club does not belong to the given community", null);
        }

        var joinedClub = joinedClubsRepo.findByUserAndMyClub(user,club);
        if (joinedClub == null) {
            return new ApiResponse(false, "User is not a member of this club", null);
        }

        if (club.getClubLeader().equals(user)) {
            return new ApiResponse(false, "Club leader cannot leave the club. Transfer leadership first.", null);
        }

        joinedClubsRepo.delete(joinedClub);

        return new ApiResponse(true, "Successfully left the club", null);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse searchCommunity(String name) {

        if(name == null || name.isEmpty()){
            return new ApiResponse(false,"Community name cannot be empty.",null);
        }

        List<Map<String,Object>> communities = communityRepo.findByCommunityNameContainingIgnoreCase(name)
                .stream()
                .map(community -> {
                    Map<String,Object> communityInfo = new HashMap<>();
                    communityInfo.put("communityId",community.getCommunityId());
                    communityInfo.put("communityName", community.getCommunityName());
                    communityInfo.put("description", community.getDescription());
                    communityInfo.put("image", community.getImage());
                    return  communityInfo;

                })
                .collect(Collectors.toList());

        if (communities.isEmpty()) {
            return new ApiResponse(false, "No communities found with the given name", null);
        }

        return new ApiResponse(true, "Communities retrieved successfully", communities);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse viewMyCommunity(Long userId) {

        if(userId == null){
            return new ApiResponse(false, "Invalid request. User ID is required.", null);
        }

        var user = userRepo.findById(userId).orElse(null);
        if(user == null){
            return new ApiResponse(false, "User not found", null);
        }

        List<Map<String, Object>> communities = communityMembersRepo.findByUser(user)
                .stream()
                .map(member -> {
                    Map<String, Object> communityInfo = new HashMap<>();
                    communityInfo.put("communityId", member.getCommunity().getCommunityId());
                    communityInfo.put("communityName", member.getCommunity().getCommunityName());
                    communityInfo.put("description", member.getCommunity().getDescription());
                    communityInfo.put("image", member.getCommunity().getImage());
                    return communityInfo;
                })
                .collect(Collectors.toList());

        if (communities.isEmpty()) {
            return new ApiResponse(false, "User has not joined any communities", null);
        }

        return new ApiResponse(true, "Communities retrieved successfully", communities);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse viewProfile(Long userId) {

        if(userId == null){
            return new ApiResponse(false, "Invalid request. User ID is required.", null);
        }

        var user = userRepo.findById(userId).orElse(null);
        if (user == null) {
            return new ApiResponse(false, "User not found", null);
        }

        Map<String, Object> profileInfo = new HashMap<>();
        profileInfo.put("userId", user.getId());
        profileInfo.put("userName", user.getName());
        profileInfo.put("email", user.getEmail());
        profileInfo.put("profileImage", user.getProfile_image());
        profileInfo.put("role", user.getRole().toString());

        return new ApiResponse(true, "Profile retrieved successfully", profileInfo);
    }

    @Override
    @Transactional
    public ApiResponse editProfile(Long userId, String photo) {

        if (userId == null || photo == null || photo.isEmpty()) {
            return new ApiResponse(false, "Invalid request. User ID and photo are required.", null);
        }

        var user = userRepo.findById(userId).orElse(null);
        if (user == null) {
            return new ApiResponse(false, "User not found", null);
        }

        user.setProfile_image(photo);
        userRepo.save(user);


        return new ApiResponse(true, "Profile photo updated successfully", Map.of(
                "userId", user.getId(),
                "profileImage", user.getProfile_image()
        ));
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse viewClubDetails(Long userId, Long clubId) {

        if (userId == null || clubId == null) {
            return new ApiResponse(false, "Invalid request. User ID and Club ID are required.", null);
        }

        var user = userRepo.findById(userId).orElse(null);
        var club = myClubRepo.findById(clubId).orElse(null);

        if (user == null) {
            return new ApiResponse(false, "User not found", null);
        }
        if (club == null) {
            return new ApiResponse(false, "Club not found", null);
        }

        boolean isMember = joinedClubsRepo.existsByUserAndMyClub(user, club);
        if (!isMember) {
            return new ApiResponse(false, "You are not a member of this club", null);
        }

        Map<String, Object> clubDetails = new HashMap<>();
        clubDetails.put("clubId", club.getId());
        clubDetails.put("clubName", club.getName());
        clubDetails.put("clubDescription", club.getDescription());
        clubDetails.put("clubLogo", club.getLogo());
        clubDetails.put("leaderId", club.getClubLeader().getId());
        clubDetails.put("leaderName", club.getClubLeader().getName());

        return new ApiResponse(true, "Club details retrieved successfully", clubDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse viewCLubMembers(Long clubId) {

        if (clubId == null) {
            return new ApiResponse(false, "Invalid request. Club ID is required.", null);
        }

        var club = myClubRepo.findById(clubId).orElse(null);
        if (club == null) {
            return new ApiResponse(false, "Club not found", null);
        }

        List<Map<String, Object>> members = joinedClubsRepo.findByMyClub(club)
                .stream()
                .map(member -> {
                    Map<String, Object> memberInfo = new HashMap<>();
                    memberInfo.put("userId", member.getUser().getId());
                    memberInfo.put("userName", member.getUser().getName());
                    memberInfo.put("profileImage", member.getUser().getProfile_image());
                    return memberInfo;
                })
                .collect(Collectors.toList());

        if (members.isEmpty()) {
            return new ApiResponse(false, "No members found in this club", null);
        }

        return new ApiResponse(true, "Club members retrieved successfully", members);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse viewJoinedClub(Long userId, Long communityId) {

        if (userId == null || communityId == null) {
            return new ApiResponse(false, "Invalid request. User ID and Community ID are required.", null);
        }

        var user = userRepo.findById(userId).orElse(null);
        var community = communityRepo.findById(communityId).orElse(null);

        if (user == null) {
            return new ApiResponse(false, "User not found", null);
        }
        if (community == null) {
            return new ApiResponse(false, "Community not found", null);
        }

        List<Map<String, Object>> joinedClubs = joinedClubsRepo.findByUserAndMyClub_Community(user, community)
                .stream()
                .map(joinedClub -> {

                    MyClub club = joinedClub.getMyClub();
                    Map<String, Object> clubInfo = new HashMap<>();
                    clubInfo.put("clubId", club.getId());
                    clubInfo.put("clubName", club.getName());
                    clubInfo.put("clubLogo", club.getLogo());
                    clubInfo.put("clubDescription", club.getDescription());


                    int totalMembers = joinedClubsRepo.countByMyClub(club);
                    clubInfo.put("totalMembers", totalMembers);

                    return clubInfo;
                })
                .collect(Collectors.toList());

        if (joinedClubs.isEmpty()) {
            return new ApiResponse(false, "User has not joined any clubs in this community", null);
        }

        return new ApiResponse(true, "Joined clubs retrieved successfully", joinedClubs);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse searchClub(Long communityId, String clubName) {

        if (communityId == null || clubName == null || clubName.trim().isEmpty()) {
            return new ApiResponse(false, "Invalid request. Community ID and Club Name are required.", null);
        }

        var community = communityRepo.findById(communityId).orElse(null);
        if (community == null) {
            return new ApiResponse(false, "Community not found", null);
        }

        List<Map<String, Object>> matchedClubs = myClubRepo.findByCommunityAndNameContainingIgnoreCase(community, clubName)
                .stream()
                .map(club -> {
                    Map<String, Object> clubInfo = new HashMap<>();
                    clubInfo.put("clubId", club.getId());
                    clubInfo.put("clubName", club.getName());
                    clubInfo.put("clubLogo", club.getLogo());
                    clubInfo.put("clubDescription", club.getDescription());


                    int totalMembers = joinedClubsRepo.countByMyClub(club);
                    clubInfo.put("totalMembers", totalMembers);

                    return clubInfo;
                })
                .collect(Collectors.toList());


        if (matchedClubs.isEmpty()) {
            return new ApiResponse(false, "No clubs found matching this name in the community", null);
        }

        return new ApiResponse(true, "Matching clubs retrieved successfully", matchedClubs);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse viewClub(Long clubId) {

        if (clubId == null) {
            return new ApiResponse(false, "Invalid request. Club ID is required.", null);
        }

        var club = myClubRepo.findById(clubId).orElse(null);
        if (club == null) {
            return new ApiResponse(false, "Club not found", null);
        }

        Map<String, Object> clubDetails = new HashMap<>();
        clubDetails.put("clubId", club.getId());
        clubDetails.put("clubName", club.getName());
        clubDetails.put("clubLogo", club.getLogo());
        clubDetails.put("clubDescription", club.getDescription());


        return new ApiResponse(true, "Club details retrieved successfully", clubDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse viewAllCommunities() {

        List<Map<String, Object>> allCommunities = communityRepo.findAll()
                .stream()
                .map(community -> {
                    Map<String, Object> communityInfo = new HashMap<>();
                    communityInfo.put("communityId", community.getCommunityId());
                    communityInfo.put("communityName", community.getCommunityName());
                    communityInfo.put("description", community.getDescription());
                    communityInfo.put("image", community.getImage());
                    return communityInfo;
                })
                .collect(Collectors.toList());

        if (allCommunities.isEmpty()) {
            return new ApiResponse(false, "No communities available", null);
        }

        return new ApiResponse(true, "All communities retrieved successfully", allCommunities);
    }


}
