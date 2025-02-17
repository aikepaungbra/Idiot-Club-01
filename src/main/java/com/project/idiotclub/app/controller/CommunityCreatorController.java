package com.project.idiotclub.app.controller;

import com.project.idiotclub.app.auth.CommunityCreatorAuth;
import com.project.idiotclub.app.auth.CommunityCreatorSignInDto;
import com.project.idiotclub.app.auth.CommunityCreatorSignUpDto;
import com.project.idiotclub.app.response.ApiResponse;
import com.project.idiotclub.app.service.CommunityCreatorService;
import com.project.idiotclub.app.service.CommunityCreatorServiceImpl;
import com.project.idiotclub.app.util.CheckForm;
import com.project.idiotclub.app.util.CommunityCreateDto;
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

    @PostMapping("/newjoinreqeust")
    public ResponseEntity<?> newMemberRequestToDecide(@Valid @RequestBody CheckForm checkForm){

        var response = communityCreatorService.checkJoinCommunityRequest(checkForm);
        var status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(response);
    }

}
