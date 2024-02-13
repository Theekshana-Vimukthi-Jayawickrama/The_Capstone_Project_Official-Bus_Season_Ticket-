package com.bus_season_ticket.capstone_project.User;


import com.bus_season_ticket.capstone_project.JourneyMaker.SelectDays;
import com.bus_season_ticket.capstone_project.JourneyMaker.UserJourney;
import com.bus_season_ticket.capstone_project.OTPGenerator.OTP;
import com.bus_season_ticket.capstone_project.QRcode.QRCode;
import com.bus_season_ticket.capstone_project.Subscription.UserSubscription;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;


    @Column(unique = true)
    private  String email;
    @Column(unique = true)
    private String userName;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<UserRoles> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<UserJourney> journeys = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name ="user_userPersonalDetails", joinColumns = {@JoinColumn(name = "fk_user")},
            inverseJoinColumns = {@JoinColumn(name = "fk_userPersonalDetails")})
    private PersonalDetails personalDetails;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name ="user_status", joinColumns = {@JoinColumn(name = "fk_user")},
            inverseJoinColumns = {@JoinColumn(name = "fk_status")})
    private UserStatusMaintain userStatusMaintain;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name ="stu_otp", joinColumns = {@JoinColumn(name = "fk_stu")},
    inverseJoinColumns = {@JoinColumn(name = "fk_otp")})
    private OTP otp;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name ="user_selectDay", joinColumns = {@JoinColumn(name = "fk_user")},
            inverseJoinColumns = {@JoinColumn(name = "fk_selectDays")})
    private SelectDays selectDays;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name ="stu_QR", joinColumns = {@JoinColumn(name = "fk_stu")},
            inverseJoinColumns = {@JoinColumn(name = "fk_QR")})
    private QRCode qrCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name ="stu_bus", joinColumns = {@JoinColumn(name = "fk_stu")},
            inverseJoinColumns = {@JoinColumn(name = "fk_bus")})
    private UserBusDetails userBusDetails;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name ="stu_subscription", joinColumns = {@JoinColumn(name = "fk_stu")},
            inverseJoinColumns = {@JoinColumn(name = "fk_subscription")})
    private UserSubscription studentSubscription;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_school_id")
    private SchoolDetails schoolDetails;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name ="stu_birthFile", joinColumns = {@JoinColumn(name = "fk_stu")},
            inverseJoinColumns = {@JoinColumn(name = "fk_BirthFile")})
    private StudentBirthFiles studentBirthFiles;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name ="User_userPhoto", joinColumns = {@JoinColumn(name = "fk_user")},
            inverseJoinColumns = {@JoinColumn(name = "fk_userPhoto")})
    private UserPhotos userPhoto;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name ="Adult_NICBackSidePhoto", joinColumns = {@JoinColumn(name = "fk_Adult_Id")},
            inverseJoinColumns = {@JoinColumn(name = "fk_userNICBackPhoto")})
    private NICAdultBack adultBackNIC;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name ="Adult_NICFrontPhoto", joinColumns = {@JoinColumn(name = "fk_Adult")},
            inverseJoinColumns = {@JoinColumn(name = "fk_NICFrontPhoto")})
    private NICAdultFront adultFrontNIC;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name ="student_approvalLetter", joinColumns = {@JoinColumn(name = "fk_student")},
            inverseJoinColumns = {@JoinColumn(name = "fk_approvalLetter")})
    private ApprovalLetter approvalLetter;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name ="stu_guardianDetails", joinColumns = {@JoinColumn(name = "fk_stu")},
            inverseJoinColumns = {@JoinColumn(name = "fk_guardianDetails")})
    private  GuardianDetails guardianDetails;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
