package com.bus_season_ticket.capstone_project.Deport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DeleteDeportRequest {
    private UUID adminId;
    private String reason;
    private String name;
    private LocalDate date;
}
