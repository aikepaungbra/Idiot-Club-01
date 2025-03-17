package com.project.idiotclub.app.util.clubleader;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnnouncementForm {

    @NotNull(message = "Leader ID is required")
    private Long leaderId;
    @NotNull(message = "Club ID is required")
    private Long clubId;
    @NotNull(message = "Community ID is required")
    private Long communityId;
    @NotBlank(message = "Message cannot be empty")
    private String message;

}
