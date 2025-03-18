package com.project.idiotclub.app.service.leaderservice;

import com.project.idiotclub.app.entity.ClubRole;
import com.project.idiotclub.app.entity.member.JoinClubRequest;
import com.project.idiotclub.app.entity.member.JoinedClubs;
import com.project.idiotclub.app.entity.leader.Post;
import com.project.idiotclub.app.entity.RequestStatus;
import com.project.idiotclub.app.repo.community.CommunityRepo;
import com.project.idiotclub.app.repo.leader.MyClubRepo;
import com.project.idiotclub.app.repo.leader.PostRepo;
import com.project.idiotclub.app.repo.member.JoinClubRequestRepo;
import com.project.idiotclub.app.repo.member.JoinedClubsRepo;
import com.project.idiotclub.app.repo.member.UserRepo;
import com.project.idiotclub.app.response.ApiResponse;
import com.project.idiotclub.app.util.clubleader.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClubLeaderServiceImpl implements ClubLeaderService{

    private final CommunityRepo communityRepo;
    private final UserRepo userRepo;
    private final MyClubRepo myClubRepo;
    private final JoinClubRequestRepo joinClubRequestRepo;
    private final JoinedClubsRepo joinedClubsRepo;
    private final PostRepo postRepo;




    @Override
    @Transactional
    public ApiResponse decideNewJoinClubRequest(NewJoinClubRequestDecideForm form) {

        if (form.getCommunityId() == null || form.getClubId() == null ||
                form.getJoinClubRequestId() == null || form.getClubLeaderId() == null) {
            return new ApiResponse(false, "Invalid request. One or more required IDs are null.", null);
        }

        var community = communityRepo.findById(form.getCommunityId()).orElse(null);
        var club = myClubRepo.findById(form.getClubId()).orElse(null);
        var joinRequest = joinClubRequestRepo.findById(form.getJoinClubRequestId()).orElse(null);
        var clubLeader = userRepo.findById(form.getClubLeaderId()).orElse(null);

        if (community == null || club == null || joinRequest == null || clubLeader == null) {
            return new ApiResponse(true,"Somthing went wrong",null);
        }
        if(club.getCommunity() != community){
            return new ApiResponse(false, "This club does not belong to the given community", null);
        }
        if(!club.getClubLeader().equals(clubLeader)){
            return new ApiResponse(false, "Only the club leader can approve or reject requests", null);
        }

        var decision = form.getRequestStatus();
        joinRequest.setRequestStatus(decision);
        joinClubRequestRepo.save(joinRequest);

        if(decision == RequestStatus.APPROVED){

            var joinedClub = new JoinedClubs();
            joinedClub.setUser(joinRequest.getUser());
            joinedClub.setRole(ClubRole.MEMBER);
            joinedClub.setMyClub(club);
            joinedClubsRepo.save(joinedClub);

            joinClubRequestRepo.delete(joinRequest);

            return new ApiResponse(true, "Join request approved and removed from pending requests.", null);
        }
        if (decision == RequestStatus.REJECTED) {
            joinClubRequestRepo.delete(joinRequest);
            return new ApiResponse(true, "Join request rejected successfully.", null);
        }

        return new ApiResponse(true, "Invalid request status.", null);
    }

    @Override
    @Transactional
    public ApiResponse makeAnnouncement(AnnouncementForm form) {

        if (form.getLeaderId() == null || form.getClubId() == null ||
                form.getCommunityId() == null || form.getMessage() == null || form.getMessage().isEmpty()) {
            return new ApiResponse(false, "Invalid request. One or more required fields are missing.", null);
        }

        var leader = userRepo.findById(form.getLeaderId()).orElse(null);
        var club = myClubRepo.findById(form.getClubId()).orElse(null);
        var community = communityRepo.findById(form.getCommunityId()).orElse(null);

        if (leader == null) {
            return new ApiResponse(false, "Club leader not found", null);
        }
        if (club == null) {
            return new ApiResponse(false, "Club not found", null);
        }
        if (community == null) {
            return new ApiResponse(false, "Community not found", null);
        }

        if (!club.getCommunity().equals(community)) {
            return new ApiResponse(false, "This club does not belong to the given community", null);
        }

        if (!club.getClubLeader().equals(leader)) {
            return new ApiResponse(false, "Only the club leader can make announcements", null);
        }

        var post = new Post();
        post.setMessage(form.getMessage());
        post.setUser(leader);
        post.setMyClub(club);
        post.setCreatedAt(LocalDateTime.now());
        postRepo.save(post);

        return new ApiResponse(true, "Announcement posted successfully", post);
    }

    @Override
    @Transactional
    public ApiResponse deleteAnnouncement(Long postId, Long leaderId) {

        if (postId == null || leaderId == null) {
            return new ApiResponse(false, "Invalid request. Post ID or Leader ID is missing.", null);
        }

        var post = postRepo.findById(postId).orElse(null);
        var leader = userRepo.findById(leaderId).orElse(null);

        if (post == null) {
            return new ApiResponse(false, "Post not found", null);
        }
        if (leader == null) {
            return new ApiResponse(false, "Club leader not found", null);
        }

        if (!post.getMyClub().getClubLeader().equals(leader)) {
            return new ApiResponse(false, "Only the club leader can delete this message", null);
        }

        postRepo.delete(post);

        return new ApiResponse(true, "Post deleted successfully", null);
    }

    @Override
    public ApiResponse removeClubMember(RemoveClubMemberForm form) {

        if (form.getLeaderId() == null || form.getClubId() == null || form.getMemberId() == null) {
            return new ApiResponse(false, "Invalid request. One or more required fields are missing.", null);
        }

        var leader = userRepo.findById(form.getLeaderId()).orElse(null);
        var club = myClubRepo.findById(form.getClubId()).orElse(null);
        var member = userRepo.findById(form.getMemberId()).orElse(null);

        if (leader == null) {
            return new ApiResponse(false, "Club leader not found", null);
        }
        if (club == null) {
            return new ApiResponse(false, "Club not found", null);
        }
        if (member == null) {
            return new ApiResponse(false, "Member not found", null);
        }

        var joinedClub = joinedClubsRepo.findByUserAndMyClub(member, club);

        if (joinedClub == null) {
            return new ApiResponse(false, "User is not a member of this club", null);
        }

        if (leader.equals(member)) {
            return new ApiResponse(false, "Club leader cannot remove themselves. Transfer leadership first.", null);
        }

        joinedClubsRepo.delete(joinedClub);

        return new ApiResponse(true, "Member removed successfully", null);
    }

    @Override
    public ApiResponse promoteClubLeader(ChangeLeaderForm form) {

        if (form.getCurrentLeaderId() == null || form.getClubId() == null || form.getNewLeaderId() == null) {
            return new ApiResponse(false, "Invalid request. One or more required fields are missing.", null);
        }

        var currentLeader = userRepo.findById(form.getCurrentLeaderId()).orElse(null);
        var club = myClubRepo.findById(form.getClubId()).orElse(null);
        var newLeader = userRepo.findById(form.getNewLeaderId()).orElse(null);

        if (currentLeader == null) {
            return new ApiResponse(false, "Current leader not found", null);
        }
        if (club == null) {
            return new ApiResponse(false, "Club not found", null);
        }
        if (newLeader == null) {
            return new ApiResponse(false, "New leader not found", null);
        }

        if (!club.getClubLeader().equals(currentLeader)) {
            return new ApiResponse(false, "Only the current club leader can promote a new leader", null);
        }

        var newLeaderMembership = joinedClubsRepo.findByUserAndMyClub(newLeader,club);
        if (newLeaderMembership == null) {
            return new ApiResponse(false, "New leader is not a member of this club", null);
        }

        var currentLeaderMembership = joinedClubsRepo.findByUserAndMyClub(currentLeader,club);
        if (currentLeaderMembership != null) {
            currentLeaderMembership.setRole(ClubRole.MEMBER);
            joinedClubsRepo.save(currentLeaderMembership);
        }

        newLeaderMembership.setRole(ClubRole.CLUB_LEADER);
        joinedClubsRepo.save(newLeaderMembership);

        club.setClubLeader(newLeader);
        myClubRepo.save(club);

        return new ApiResponse(true, "New club leader promoted successfully", null);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse viewMyClubDescription(Long clubId) {

        if(clubId == null){
            return new ApiResponse(false, "Invalid request. Club ID is missing.", null);
        }

        var club = myClubRepo.findById(clubId).orElse(null);

        if(club == null){
            return new ApiResponse(false, "Club not found", null);
        }

        return new ApiResponse(true, "Club description retrieved successfully", club.getDescription());
    }

    @Override
    @Transactional()
    public ApiResponse editMyClubDescription(EditMyClubDescriptionForm form) {

        if(form.getLeaderId() == null || form.getClubId() == null || form.getNewDescription() == null){
            return new ApiResponse(false, "Invalid request. Required fields are missing.", null);
        }

        var club = myClubRepo.findById(form.getClubId()).orElse(null);
        var leader = userRepo.findById(form.getLeaderId()).orElse(null);

        if (club == null) {
            return new ApiResponse(false, "Club not found", null);
        }

        if (leader == null) {
            return new ApiResponse(false, "Leader not found", null);
        }

        if (!club.getClubLeader().equals(leader)) {
            return new ApiResponse(false, "Only the club leader can edit the description", null);
        }

        club.setDescription(form.getNewDescription());
        myClubRepo.save(club);

        return new ApiResponse(true, "Club description updated successfully", null);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse viewReasonToJoin(Long leaderId, Long clubId) {

        if (leaderId == null || clubId == null) {
            return new ApiResponse(false, "Invalid request. Required fields are missing.", null);
        }

        var club = myClubRepo.findById(clubId).orElse(null);
        var leader = userRepo.findById(leaderId).orElse(null);

        if (club == null) {
            return new ApiResponse(false, "Club not found", null);
        }

        if (leader == null) {
            return new ApiResponse(false, "Leader not found", null);
        }

        if (!club.getClubLeader().equals(leader)) {
            return new ApiResponse(false, "Only the club leader can view join reasons", null);
        }

        List<Map<String, Object>> joinRequests = joinClubRequestRepo.findByMyClub(club)
                .stream()
                .map(req -> {
                    Map<String, Object> requestMap = new HashMap<>();
                    requestMap.put("requestId", req.getId());
                    requestMap.put("userId", req.getUser().getId());
                    requestMap.put("userName", req.getUser().getName());
                    requestMap.put("reasonToJoin", req.getReasonToJoin());
                    requestMap.put("requestStatus", req.getRequestStatus().toString());
                    return requestMap;
                })
                .collect(Collectors.toList());


        return new ApiResponse(true, "Join reasons retrieved successfully", joinRequests);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse viewClubMembers(Long leaderId, Long clubId) {

        if (leaderId == null || clubId == null) {
            return new ApiResponse(false, "Invalid request. Required fields are missing.", null);
        }

        var club = myClubRepo.findById(clubId).orElse(null);
        var leader = userRepo.findById(leaderId).orElse(null);


        if (club == null) {
            return new ApiResponse(false, "Club not found", null);
        }

        if (leader == null) {
            return new ApiResponse(false, "Leader not found", null);
        }

        if (!club.getClubLeader().equals(leader)) {
            return new ApiResponse(false, "Only the club leader can view club members", null);
        }

        List<Map<String, Object>> clubMembers = joinedClubsRepo.findByMyClub(club)
                .stream()
                .map(member -> {
                    Map<String, Object> memberInfo = new HashMap<>();
                    memberInfo.put("userId", member.getUser().getId());
                    memberInfo.put("userName", member.getUser().getName());
                    memberInfo.put("profileImage", member.getUser().getProfile_image());
                    memberInfo.put("role", member.getRole().toString());
                    return memberInfo;
                })
                .collect(Collectors.toList());

        return new ApiResponse(true, "Club members retrieved successfully", clubMembers);
    }


}
