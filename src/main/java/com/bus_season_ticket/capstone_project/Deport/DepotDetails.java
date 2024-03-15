package com.bus_season_ticket.capstone_project.Deport;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class DepotDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int phoneNumber;
    private int phoneNumber2;
    @Column(unique = true)
    private String depotName;
    private UUID adminId;
}
