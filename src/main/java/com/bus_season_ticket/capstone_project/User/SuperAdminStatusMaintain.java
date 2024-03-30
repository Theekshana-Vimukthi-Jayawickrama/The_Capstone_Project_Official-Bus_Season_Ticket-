package com.bus_season_ticket.capstone_project.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.web.WebProperties;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class SuperAdminStatusMaintain {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private boolean status;
}
