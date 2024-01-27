package com.bus_season_ticket.capstone_project.OTPGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OTP {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer otpCode;
    private String email;

    @Column(name = "otp_expiry_time")
    private LocalDateTime otpExpiryTime;

//    @OneToOne(mappedBy = "otp")
//    private User user;
}
