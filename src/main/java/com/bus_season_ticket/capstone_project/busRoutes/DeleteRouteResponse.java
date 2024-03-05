package com.bus_season_ticket.capstone_project.busRoutes;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DeleteRouteResponse {
    private UUID adminId;
    private String route;
    private String reason;
}
