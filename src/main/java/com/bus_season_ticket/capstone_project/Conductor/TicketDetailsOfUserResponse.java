package com.bus_season_ticket.capstone_project.Conductor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TicketDetailsOfUserResponse {
    private String month;
    private String remainsDays;

}
