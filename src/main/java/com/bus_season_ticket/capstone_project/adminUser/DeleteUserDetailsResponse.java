package com.bus_season_ticket.capstone_project.adminUser;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteUserDetailsResponse {

    private String userName;
    private String role;
    private UUID userId;
    private String fullName;
    private String reason;
    private UUID adminId;
    private LocalDate deleteDate;
}
