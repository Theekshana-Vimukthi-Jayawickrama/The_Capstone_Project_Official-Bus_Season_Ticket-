package com.bus_season_ticket.capstone_project.User;

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
public class UserBusDetails {
    @Id
    @GeneratedValue
    private Integer id;
    private String route;
    private String distance;
    private Double charge;
    private String nearestDeport;
}
