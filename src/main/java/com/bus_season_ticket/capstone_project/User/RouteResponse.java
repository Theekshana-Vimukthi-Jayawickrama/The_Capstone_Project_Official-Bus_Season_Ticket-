package com.bus_season_ticket.capstone_project.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteResponse {
    private String route;
    private String distance;
    private Double charge;
    private String routeNo;
}
