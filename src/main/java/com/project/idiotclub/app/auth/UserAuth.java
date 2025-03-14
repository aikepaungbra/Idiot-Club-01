package com.project.idiotclub.app.auth;


import com.project.idiotclub.app.entity.ClubRole;
import com.project.idiotclub.app.entity.User;
import com.project.idiotclub.app.repo.UserRepo;
import com.project.idiotclub.app.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAuth{

    @Autowired
    private UserRepo userRepo;

    public ApiResponse singUp(String name, String email, String password) {

        if(userRepo.findByEmail(email).isPresent()){
            return new ApiResponse(false,"already exist this email",null);
        }

        var user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(ClubRole.MEMBER);
        user.setProfile_image(null);
        userRepo.save(user);

        return new ApiResponse(true,"successfully sign up",user);
    }

    public ApiResponse login(String email, String password) {

        var user = userRepo.findByEmail(email);

        if(user.isEmpty()){
            return  new ApiResponse(false,"there is no such email Please Sign Up",null);
        }
        if(!user.get().getPassword().equals(password)){
            return new ApiResponse(false,"incorrect password",null);
        }

        return new ApiResponse(true,"successfully login",user.get());
    }

}
