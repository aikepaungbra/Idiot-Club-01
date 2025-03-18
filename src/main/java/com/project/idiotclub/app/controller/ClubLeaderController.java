package com.project.idiotclub.app.controller;

import com.project.idiotclub.app.response.ApiResponse;
import com.project.idiotclub.app.service.leaderservice.ClubLeaderService;
import com.project.idiotclub.app.util.clubleader.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/delete-post/{postId}/{leaderId}")
    public ResponseEntity<ApiResponse> deleteAnnouncement(
            @PathVariable Long postId,
            @PathVariable Long leaderId) {

        var response = clubLeaderService.deleteAnnouncement(postId, leaderId);
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return  ResponseEntity.status(status).body(response);
    }

    @DeleteMapping("/remove-club-member/{leaderId}/{clubId}/{memberId}")
    public ResponseEntity<ApiResponse> removeClubMember(
            @PathVariable Long leaderId,
            @PathVariable Long clubId,
            @PathVariable Long memberId
    ){
        var form = new RemoveClubMemberForm();
        form.setLeaderId(leaderId);
        form.setClubId(clubId);
        form.setMemberId(memberId);

        var response = clubLeaderService.removeClubMember(form);
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(response);
    }

    @PutMapping("/promote-club-leader")
    public ResponseEntity<ApiResponse> promoteClubLeader(@RequestBody @Valid ChangeLeaderForm form) {

        var response = clubLeaderService.promoteClubLeader(form);
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/club-description/{clubId}")
    public ResponseEntity<ApiResponse> viewMyClubDescription(@PathVariable Long clubId){

        var response = clubLeaderService.viewMyClubDescription(clubId);
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(response);
    }

    @PutMapping("/edit-club-description")
    public ResponseEntity<ApiResponse> editMyClubDescription(@RequestBody @Valid EditMyClubDescriptionForm form){

        var response = clubLeaderService.editMyClubDescription(form);
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/view-reason-to-join/{leaderId}/{clubId}")
    public ResponseEntity<ApiResponse> viewReasonToJoin(@PathVariable Long leaderId, @PathVariable Long clubId) {
        var response = clubLeaderService.viewReasonToJoin(leaderId, clubId);
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/view-club-members/{leaderId}/{clubId}")
    public ResponseEntity<ApiResponse> viewClubMembers(@PathVariable Long leaderId, @PathVariable Long clubId) {
        var response = clubLeaderService.viewClubMembers(leaderId, clubId);
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

}
