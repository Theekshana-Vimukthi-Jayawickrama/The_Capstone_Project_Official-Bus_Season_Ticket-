package com.bus_season_ticket.capstone_project.adminUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUserResponse {
    private String userName;
    private UUID id;
    private String status;
    private List<String> role;
    private byte[] data;
    private String PhotoType;
}
