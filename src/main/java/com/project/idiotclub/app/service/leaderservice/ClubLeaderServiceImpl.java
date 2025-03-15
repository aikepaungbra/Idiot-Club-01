package com.project.idiotclub.app.service.leaderservice;

import com.project.idiotclub.app.entity.ClubRole;
import com.project.idiotclub.app.entity.JoinedClubs;
import com.project.idiotclub.app.entity.RequestStatus;
import com.project.idiotclub.app.repo.*;
import com.project.idiotclub.app.response.ApiResponse;
import com.project.idiotclub.app.util.clubleader.NewJoinClubRequestDecideForm;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClubLeaderServiceImpl implements ClubLeaderService{

    private final CommunityRepo communityRepo;
    private final UserRepo userRepo;
    private final MyClubRepo myClubRepo;
    private final JoinClubRequestRepo joinClubRequestRepo;
    private final JoinedClubsRepo joinedClubsRepo;




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

}
