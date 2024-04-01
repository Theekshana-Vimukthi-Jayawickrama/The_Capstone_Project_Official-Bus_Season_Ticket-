package com.bus_season_ticket.capstone_project.adminUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetShortConductorDetailsResponse {
    private String fullName;
    private String userName;
    private String role;
    private String photoType;
    private byte[] data;
    private UUID id;
}
