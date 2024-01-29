package com.bus_season_ticket.capstone_project.busRoutes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusRouteRequest {
    private String route;
    private String routeNo;
    private String distance;
    private Double perDayCharge;
}
