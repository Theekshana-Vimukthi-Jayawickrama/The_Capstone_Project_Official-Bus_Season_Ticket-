package com.bus_season_ticket.capstone_project.adminUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetAllConductorDetailsResponse {
    private String fullName;
    private String intName;
    private String dob;
    private String address;
    private String gender;
    private String telephoneNumber;
    private String residence;
    private String email;
    private String role;
    private String conductorId;
    private String photoType;
    private byte[] data;


}
