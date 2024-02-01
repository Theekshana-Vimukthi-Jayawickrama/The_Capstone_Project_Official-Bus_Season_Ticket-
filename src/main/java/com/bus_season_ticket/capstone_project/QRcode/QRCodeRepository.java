package com.bus_season_ticket.capstone_project.QRcode;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QRCodeRepository extends JpaRepository<QRCode, Integer> {
    Optional<QRCode> findById (Integer id);
}