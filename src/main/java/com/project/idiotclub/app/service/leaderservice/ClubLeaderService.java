package com.project.idiotclub.app.service.leaderservice;

import com.project.idiotclub.app.response.ApiResponse;
import com.project.idiotclub.app.util.clubleader.NewJoinClubRequestDecideForm;

public interface ClubLeaderService {

    ApiResponse decideNewJoinClubRequest(NewJoinClubRequestDecideForm form);

}
