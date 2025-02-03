package com.project.idiotclub.app.controller;

import com.project.idiotclub.app.auth.CommunityCreatorAuth;
import com.project.idiotclub.app.auth.CommunityCreatorSignInDto;
import com.project.idiotclub.app.auth.CommunityCreatorSignUpDto;
import com.project.idiotclub.app.response.ApiResponse;
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

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signUp(@Valid @RequestBody CommunityCreatorSignUpDto dto) {

          ApiResponse apiResponse =  communityCreatorAuth.signUp(dto.getName(), dto.getEmail(), dto.getPassword()) ;
          HttpStatus status = apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        communityCreatorAuth.signUp(dto.getName(), dto.getEmail(), dto.getPassword());
        return ResponseEntity.status(status).body(apiResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody CommunityCreatorSignInDto dto) {

        ApiResponse response = communityCreatorAuth.login(dto.getEmail(), dto.getPassword());
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(response);
    }

}
