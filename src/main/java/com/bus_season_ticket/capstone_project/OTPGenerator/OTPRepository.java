package com.bus_season_ticket.capstone_project.OTPGenerator;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OTPRepository extends JpaRepository<OTP,Integer> {
    Optional<OTP> findByEmail(String email);
}
