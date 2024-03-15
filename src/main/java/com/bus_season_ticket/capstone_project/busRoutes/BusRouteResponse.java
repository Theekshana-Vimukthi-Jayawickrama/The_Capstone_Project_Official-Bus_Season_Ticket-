package com.bus_season_ticket.capstone_project.busRoutes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusRouteResponse {
    private int id;
    private String routeDistance;
    private String routeSource;
    private String routeNo;
    private String distance;
    private Double perDayCharge;
}
