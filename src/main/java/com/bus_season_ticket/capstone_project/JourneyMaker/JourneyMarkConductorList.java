package com.bus_season_ticket.capstone_project.JourneyMaker;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class JourneyMarkConductorList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private UUID conductorId;

    @ManyToOne
    @JoinColumn(name = "journey_id")
    private UserJourney userJourney;
}
