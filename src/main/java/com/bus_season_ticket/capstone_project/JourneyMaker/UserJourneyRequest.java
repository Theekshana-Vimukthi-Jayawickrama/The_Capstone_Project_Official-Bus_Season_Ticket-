package com.bus_season_ticket.capstone_project.JourneyMaker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserJourneyRequest {
    private String userId;
    private boolean hasJourney;
    private UUID conductorId;
}
