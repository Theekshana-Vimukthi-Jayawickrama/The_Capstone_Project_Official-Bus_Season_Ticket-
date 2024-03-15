package com.bus_season_ticket.capstone_project.Deport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddDepotRequest {
    private int id;
    private int phoneNumber;
    private int phoneNumber2;
    private String depotName;
}
