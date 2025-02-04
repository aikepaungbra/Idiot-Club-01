package com.project.idiotclub.app.util;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JoinCommunityRequestDto {

    @NotBlank(message = "request Description is Required")
    private String requestDescription;
    @NotNull(message = "user id is required")
    private Long userId;
    @NotNull(message = "community id is required")
    private Long communityId;

}
