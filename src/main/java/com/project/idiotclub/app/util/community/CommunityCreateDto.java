package com.project.idiotclub.app.util.community;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CommunityCreateDto {

    @NotBlank(message = "Community name is required")
    private String communityName;
    @NotBlank(message = "Description is required")
    private String description;
    @NotBlank(message = "Image is required")
    private String image;
    @NotNull(message = "Community creator id is required")
    private Long communityCreatorId;

}
