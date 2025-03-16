package com.project.idiotclub.app.service.leaderservice;

import com.project.idiotclub.app.entity.ClubRole;
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
import com.project.idiotclub.app.util.clubleader.AnnouncementForm;
import com.project.idiotclub.app.util.clubleader.NewJoinClubRequestDecideForm;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
            return new ApiResponse(true, "Join request approved successfully.", null);
        }
        if (decision == RequestStatus.REJECTED) {
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

}
