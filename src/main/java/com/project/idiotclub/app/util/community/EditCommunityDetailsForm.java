package com.project.idiotclub.app.util.community;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EditCommunityDetailsForm {

    @NotNull
    private Long communityId;
    @NotNull
    private Long leaderId;
    @NotBlank
    private String newCommunityName;
    @NotBlank
    private String newCommunityDescription;
    @NotBlank
    private String newCommunityLogo;

}
