package com.bus_season_ticket.capstone_project.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<UserRoles, Integer> {
    Optional<UserRoles> findByRole(Role role);
}

