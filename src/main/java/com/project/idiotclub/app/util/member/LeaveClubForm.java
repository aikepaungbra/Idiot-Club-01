package com.project.idiotclub.app.util.member;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LeaveClubForm {

    @NotNull(message = "community id is required")
    private Long communityId;
    @NotNull(message = "club id is required")
    private Long clubId;
    @NotNull(message = "user id is required")
    private Long userId;

}
