package com.bus_season_ticket.capstone_project.adminUser;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PendingStudentDetailsResponse {
    private String fullName;
    private String intName;
    private String dob;
    private String address;
    private String status;
    private List<String> roles;
    private String pendingRole;
    private String gender;
    private String telephoneNumber;
    private String residence;
    private String route;
    private String distance;
    private Double charge;
    private String nearestDeport;
    private String schAddress;
    private String district;
    private String indexNumber;
    private String nameOfGuardian;
    private String guardianType;
    private String occupation;
    private String contactNumber;
    private byte[] photoData;
    private String photoType;
}
