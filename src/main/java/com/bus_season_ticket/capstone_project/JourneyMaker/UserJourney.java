package com.bus_season_ticket.capstone_project.JourneyMaker;


import com.bus_season_ticket.capstone_project.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_journey")
public class UserJourney {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "journey_date")
    private LocalDate journeyDate;

    @Column(name = "journey_count")
    private int journeyCount;


    private UUID userId;

    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user") // Name of the foreign key column in user_journey table
    private User user;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "journey_conductor",
            joinColumns = @JoinColumn(name = "journey_id"),
            inverseJoinColumns = @JoinColumn(name = "conductor_id")
    )
    private Set<JourneyMarkConductorList> journeyMarkConductorLists = new HashSet<>();


}
