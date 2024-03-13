package com.bus_season_ticket.capstone_project.Subscription;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class UserSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean nextMonthSubscription;

    private String month;




}
