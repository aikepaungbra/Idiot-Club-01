package com.project.idiotclub.app.util.member;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LeaveCommunityForm {

    @NotNull(message = "community id is required")
    private Long communityId;
    @NotNull(message = "user id is required")
    private Long userId;

}
