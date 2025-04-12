package com.project.idiotclub.app.controller;

import com.project.idiotclub.app.auth.CommunityCreatorAuth;
import com.project.idiotclub.app.auth.CommunityCreatorSignInDto;
import com.project.idiotclub.app.auth.CommunityCreatorSignUpDto;
import com.project.idiotclub.app.response.ApiResponse;
import com.project.idiotclub.app.service.creatorservice.CommunityCreatorService;
import com.project.idiotclub.app.util.community.CheckForm;
import com.project.idiotclub.app.util.community.CommunityCreateDto;
import com.project.idiotclub.app.util.community.DecideNewClubForm;
import com.project.idiotclub.app.util.community.EditCommunityDetailsForm;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/creator")
@Validated
public class CommunityCreatorController {

    @Autowired
    private CommunityCreatorAuth communityCreatorAuth;

    @Autowired
    private CommunityCreatorService communityCreatorService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signUp(@Valid @RequestBody CommunityCreatorSignUpDto dto) {

          var apiResponse =  communityCreatorAuth.signUp(dto.getName(), dto.getEmail(), dto.getPassword()) ;
          var status = apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(apiResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody CommunityCreatorSignInDto dto) {

        var response = communityCreatorAuth.login(dto.getEmail(), dto.getPassword());
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(response);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCommunity(@Valid @RequestBody CommunityCreateDto dto){

        var response = communityCreatorService.createCommunity(dto);
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(response);
    }

    @PostMapping("/decidejoincomreqeust")
    public ResponseEntity<?> newMemberRequestToDecide(@Valid @RequestBody CheckForm checkForm){

        var response = communityCreatorService.decideJoinCommunityRequest(checkForm);
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(response);
    }

    @PostMapping("decide-new-club-request")
    public ResponseEntity<?> createNewClubRequestToDecide(@Valid @RequestBody DecideNewClubForm form){

        var response = communityCreatorService.decideCreateNewClubRequest(form);
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(response);
    }

    @PutMapping("/edit-community")
    public ResponseEntity<ApiResponse> editCommunityDetails(@RequestBody @Valid EditCommunityDetailsForm form) {
        var response = communityCreatorService.editCommunityDetails(form);
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/view-club-request")
    public ResponseEntity<ApiResponse> viewNewClubRequestDetails(@RequestParam Long communityId, @RequestParam Long createClubRequestId) {
        var response = communityCreatorService.viewNewClubRequestDetails(communityId, createClubRequestId);
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }


    @GetMapping("/view-profile")
    public ResponseEntity<ApiResponse> viewOwnProfile(@RequestParam Long communityCreatorId) {
        var response = communityCreatorService.viewOwnProfile(communityCreatorId);
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/view-clubs")
    public ResponseEntity<ApiResponse> viewClubs(@RequestParam Long communityId) {
        var response = communityCreatorService.viewClubs(communityId);
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @PutMapping("/edit-profile")
    public ResponseEntity<ApiResponse> editProfile(@RequestParam Long creatorId, @RequestParam String photo) {
        var response = communityCreatorService.editProfile(creatorId, photo);
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }
    
    @GetMapping("/join-reason/{id}")
    public ResponseEntity<ApiResponse> viewJoinReason(@PathVariable ("id") Long communityRequestId){
    	
    	var response = communityCreatorService.viewJoinReason(communityRequestId);
    	var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    	
    	return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/view-join-request")
    public ResponseEntity<ApiResponse> ViewJoinCommunityRequest(@RequestParam Long communityId){
    	
    	var response = communityCreatorService.viewJoinCommunityRequest(communityId);
    	var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    	
    	return ResponseEntity.status(status).body(response);
    }
    
    @GetMapping("/view-all-new-club-request")
    public ResponseEntity<ApiResponse> viewAllNewClubRequest(@RequestParam Long communityId) {
    
    	var response = communityCreatorService.viewAllNewClubRequest(communityId);
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }


}
