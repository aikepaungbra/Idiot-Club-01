package com.project.idiotclub.app.auth;

import com.project.idiotclub.app.auth.dto.LoginOutputDto;
import com.project.idiotclub.app.entity.community.CommunityCreator;
import com.project.idiotclub.app.repo.community.CommunityCreatorRepo;
import com.project.idiotclub.app.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommunityCreatorAuth {

    @Autowired
    private CommunityCreatorRepo communityCreatorRepo;

    public ApiResponse signUp(String name, String email, String password) {
        if(communityCreatorRepo.findByCreatorEmail(email).isPresent()){
            return new ApiResponse(false,"already exist this email",null);
        }
        CommunityCreator communityCreator = new CommunityCreator();
        communityCreator.setCreatorEmail(email);
        communityCreator.setCreatorName(name);
        communityCreator.setCreatorPassword(password);
        communityCreatorRepo.save(communityCreator);
        
        LoginOutputDto loginOutputDto = new LoginOutputDto();
        loginOutputDto.setCommunityCreatorId(communityCreator.getCommunityCreatorId());
        loginOutputDto.setCreatorName(communityCreator.getCreatorName());
        loginOutputDto.setCreatorEmail(communityCreator.getCreatorEmail());
        loginOutputDto.setCreatorPassword(communityCreator.getCreatorPassword());
        loginOutputDto.setCreatorPhoto(communityCreator.getCreatorPhoto());
        loginOutputDto.setCommunity(communityCreator.getCommunity());
        loginOutputDto.setHasCommunity(false);
        
        
        return new ApiResponse(true,"succssfully sign up",loginOutputDto);
    }

    public ApiResponse login(String email, String password) {
        Optional<CommunityCreator> communityCreator = communityCreatorRepo.findByCreatorEmail(email);
        if(communityCreator.isEmpty()) {
            return new ApiResponse(false,"there is no such email",null);
        }
        if(!communityCreator.get().getCreatorPassword().equals(password)){
            return new ApiResponse(false,"incorrect password",null);
        }
        
        boolean hasCommunity = communityCreator.get().getCommunity() != null;
        
        LoginOutputDto loginOutputDto = new LoginOutputDto();
        loginOutputDto.setCommunityCreatorId(communityCreator.get().getCommunityCreatorId());
        loginOutputDto.setCreatorName(communityCreator.get().getCreatorName());
        loginOutputDto.setCreatorEmail(communityCreator.get().getCreatorEmail());
        loginOutputDto.setCreatorPassword(communityCreator.get().getCreatorPassword());
        loginOutputDto.setCreatorPhoto(communityCreator.get().getCreatorPhoto());
        loginOutputDto.setCommunity(communityCreator.get().getCommunity());
        loginOutputDto.setHasCommunity(hasCommunity);
        
        return new ApiResponse(true,"successfully login",loginOutputDto);
    }
}
