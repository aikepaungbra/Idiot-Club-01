package com.project.idiotclub.app.controller;

import com.project.idiotclub.app.auth.CommunityCreatorAuth;
import com.project.idiotclub.app.auth.CommunityCreatorDto;
import com.project.idiotclub.app.service.CommunityCreatorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/creator")
@Validated
public class CommunityCreatorController {

    @Autowired
    private CommunityCreatorAuth communityCreatorAuth;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody CommunityCreatorDto dto) {
        communityCreatorAuth.signUp(dto.getName(), dto.getEmail(), dto.getPassword());
        return ResponseEntity.ok("sing Up successfully");
    }

}
