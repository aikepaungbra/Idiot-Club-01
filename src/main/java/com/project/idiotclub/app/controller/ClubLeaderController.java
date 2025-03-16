package com.project.idiotclub.app.controller;

import com.project.idiotclub.app.service.leaderservice.ClubLeaderService;
import com.project.idiotclub.app.util.clubleader.AnnouncementForm;
import com.project.idiotclub.app.util.clubleader.NewJoinClubRequestDecideForm;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/leader")
@AllArgsConstructor
public class ClubLeaderController {

    private final ClubLeaderService clubLeaderService;

    @PostMapping("/decide-new-club-request")
    public ResponseEntity<?> newJoinClubRequestToDecide(@RequestBody @Valid NewJoinClubRequestDecideForm form) {

        var response = clubLeaderService.decideNewJoinClubRequest(form);
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return  ResponseEntity.status(status).body(response);
    }

    @PostMapping("/make-post")
    public ResponseEntity<?> makeAnnouncement(@RequestBody @Valid AnnouncementForm form) {

        var response = clubLeaderService.makeAnnouncement(form);
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(response);
    }

}
