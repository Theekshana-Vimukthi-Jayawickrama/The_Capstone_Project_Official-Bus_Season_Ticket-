package com.bus_season_ticket.capstone_project.QRcode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QRCodeRequest {
    private String email;
    private Long userId;
}
