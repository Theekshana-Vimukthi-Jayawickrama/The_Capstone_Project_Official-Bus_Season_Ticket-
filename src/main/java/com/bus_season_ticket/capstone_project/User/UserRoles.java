package com.bus_season_ticket.capstone_project.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Roles")
public class UserRoles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Role role;

    private UUID adminId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
