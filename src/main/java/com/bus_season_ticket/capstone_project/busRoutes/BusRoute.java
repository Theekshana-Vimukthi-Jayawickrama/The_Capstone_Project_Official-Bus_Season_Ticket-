package com.bus_season_ticket.capstone_project.busRoutes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BusRoute {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String route;
    private String routeNo;
    private String distance;
    private Double perDayCharge;
    private String routeDistance;
    private String routeSource;


}
