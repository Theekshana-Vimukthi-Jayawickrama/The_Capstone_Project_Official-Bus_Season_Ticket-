package com.bus_season_ticket.capstone_project.adminUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PendingAdultDetailsResponse {
    private String fullName;
    private String intName;
    private String dob;
    private String address;
    private String gender;
    private String telephoneNumber;
    private String residence;
    private String route;
    private String distance;
    private String status;
    private List<String> roles;
    private String pendingRole;
    private Double charge;
    private String nearestDeport;
    private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;
    private boolean saturday;
    private boolean sunday;
    private byte[] nicBackData;
    private String nicBackType;
    private byte[] nicFrontData;
    private String nicFrontType;
    private byte[] userPhotoData;
    private String userPhotoType;
}
