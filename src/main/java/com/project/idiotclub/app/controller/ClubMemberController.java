package com.project.idiotclub.app.controller;

import com.project.idiotclub.app.auth.UserAuth;
import com.project.idiotclub.app.auth.UserAuthSignInDto;
import com.project.idiotclub.app.auth.UserAuthSignUpDto;
import com.project.idiotclub.app.response.ApiResponse;
import com.project.idiotclub.app.service.memberservice.ClubMemberService;
import com.project.idiotclub.app.util.member.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/member")
@Validated
@AllArgsConstructor
public class ClubMemberController {


    private final UserAuth userAuth;
    private final ClubMemberService clubMemberService;

    @PostMapping("signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserAuthSignUpDto dto){

        var response = userAuth.singUp(dto.getName(), dto.getEmail(), dto.getPassword());
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return  ResponseEntity.status(status).body(response);

    }
    @PostMapping("login")
    public ResponseEntity<?> login(@Valid @RequestBody UserAuthSignInDto dto){

        var apiResponse = userAuth.login(dto.getEmail(), dto.getPassword());
        var status = apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return  ResponseEntity.status(status).body(apiResponse);
    }

    @PostMapping("/join-community")
    public ResponseEntity<?> joinCommunity(@Valid @RequestBody JoinCommunityRequestDto dto){

        var apiresponse = clubMemberService.joinCommunity(dto);
        var status = apiresponse.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(apiresponse);
    }

    @PostMapping("/leave-community")
    public ResponseEntity<?> leaveCommunity(@Valid @RequestBody LeaveCommunityForm form){
        var apiResponse = clubMemberService.leaveCommunity(form);
        var status = apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(apiResponse);
    }

    @PostMapping("/create-my-club")
    public ResponseEntity<?> createClub(@Valid @RequestBody CreateClubForm form){
        var apiResponse = clubMemberService.createClub(form);
        var status = apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(apiResponse);
    }

    @PostMapping("/join-club")
    public ResponseEntity<?> joinCLub(@Valid @RequestBody JoinClubForm form){

        var apiResponse = clubMemberService.joinClub(form);
        var status = apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(apiResponse);
    }

    @GetMapping("/club/read-posts")
    public ResponseEntity<ApiResponse> readPost(
            @RequestParam Long clubId,
            @RequestParam Long communityId) {

        var form = new ReadPostForm();
        form.setClubId(clubId);
        form.setCommunityId(communityId);

        var response = clubMemberService.readPost(form);
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(response);
    }

    @PostMapping("/club/leave-club")
    public ResponseEntity<ApiResponse> leaveClub(@Valid @RequestBody LeaveClubForm form){

        var response = clubMemberService.leaveClub(form);
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/search-community")
    public ResponseEntity<ApiResponse> searchCommunity(@RequestParam String name) {
        var response = clubMemberService.searchCommunity(name);
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/view-my-community/{userId}")
    public ResponseEntity<ApiResponse> viewMyCommunity(@PathVariable Long userId) {
        var response = clubMemberService.viewMyCommunity(userId);
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/view-profile/{userId}")
    public ResponseEntity<ApiResponse> viewProfile(@PathVariable Long userId) {
        var response = clubMemberService.viewProfile(userId);
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @PutMapping("/edit-profile-photo")
    public ResponseEntity<ApiResponse> editProfilePhoto(@RequestParam Long userId, @RequestParam String photo) {
        var response = clubMemberService.editProfile(userId, photo);
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/view-club-details")
    public ResponseEntity<ApiResponse> viewClubDetails(@RequestParam Long userId, @RequestParam Long clubId) {
        var response = clubMemberService.viewClubDetails(userId, clubId);
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/view-club-members/{clubId}")
    public ResponseEntity<ApiResponse> viewClubMembers(@PathVariable Long clubId) {
        var response = clubMemberService.viewCLubMembers(clubId);
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/view-joined-clubs")
    public ResponseEntity<ApiResponse> viewJoinedClub(@RequestParam Long userId, @RequestParam Long communityId) {
        var response = clubMemberService.viewJoinedClub(userId, communityId);
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }


}
