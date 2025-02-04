package com.project.idiotclub.app.controller;

import com.project.idiotclub.app.auth.UserAuth;
import com.project.idiotclub.app.auth.UserAuthSignInDto;
import com.project.idiotclub.app.auth.UserAuthSignUpDto;
import jakarta.validation.Valid;
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
public class ClubMemberController {

    @Autowired
    private UserAuth userAuth;

    @PostMapping("signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserAuthSignUpDto dto){

        var response = userAuth.singUp(dto.getName(), dto.getEmail(), dto.getPassword());
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return  ResponseEntity.status(status).body(response);

    }
    @PostMapping("login")
    ResponseEntity<?> login(@Valid @RequestBody UserAuthSignInDto dto){

        var apiResponse = userAuth.login(dto.getEmail(), dto.getPassword());
        HttpStatus status = apiResponse.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return  ResponseEntity.status(status).body(apiResponse);
    }



}
