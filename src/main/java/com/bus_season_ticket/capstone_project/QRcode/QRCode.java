package com.bus_season_ticket.capstone_project.QRcode;

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
public class QRCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;

    private UUID userId;

    @Lob
    @Column(name = "QRCodeData", columnDefinition = "LONGBLOB" )
    private String QRCodeData;

}