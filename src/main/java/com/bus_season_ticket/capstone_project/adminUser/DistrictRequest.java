package com.bus_season_ticket.capstone_project.adminUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class DistrictRequest {
    private String districtName;
    private UUID adminId;
}
