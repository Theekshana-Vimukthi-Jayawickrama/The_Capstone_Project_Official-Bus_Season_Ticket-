package com.bus_season_ticket.capstone_project.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "guardianDetails")
public class GuardianDetails {
    @Id
    @GeneratedValue
    private Integer guardianId;
    private String nameOfGuardian;
    private String guardianType;
    private String occupation;
    private String contactNumber;
}
