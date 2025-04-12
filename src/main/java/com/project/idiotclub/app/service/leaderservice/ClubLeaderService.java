package com.project.idiotclub.app.service.leaderservice;

import com.project.idiotclub.app.response.ApiResponse;
import com.project.idiotclub.app.util.clubleader.*;

public interface ClubLeaderService {

    ApiResponse decideNewJoinClubRequest(NewJoinClubRequestDecideForm form);

    ApiResponse makeAnnouncement(AnnouncementForm form);

    ApiResponse deleteAnnouncement(Long postId, Long leaderId);

    ApiResponse removeClubMember(RemoveClubMemberForm form);

    ApiResponse promoteClubLeader(ChangeLeaderForm form);

    ApiResponse viewMyClubDescription(Long clubId);

    ApiResponse editMyClubDescription(EditMyClubDescriptionForm form);

    ApiResponse viewReasonToJoin(Long leaderId,Long clubId);

    ApiResponse viewClubMembers(Long leaderId,Long clubId);
    
    ApiResponse viewMyCreationClub(ViewMyCreationClubForm form);

}
