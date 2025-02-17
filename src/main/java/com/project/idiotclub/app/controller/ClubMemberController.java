package com.project.idiotclub.app.controller;

import com.project.idiotclub.app.auth.UserAuth;
import com.project.idiotclub.app.auth.UserAuthSignInDto;
import com.project.idiotclub.app.auth.UserAuthSignUpDto;
import com.project.idiotclub.app.service.ClubMemberService;
import com.project.idiotclub.app.util.JoinCommunityRequestDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/joincommunity")
    public ResponseEntity<?> joinCommunity(@Valid @RequestBody JoinCommunityRequestDto dto){

        var apiresponse = clubMemberService.joinCommunity(dto);
        var status = apiresponse.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(apiresponse);
    }






}
