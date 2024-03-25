package com.bus_season_ticket.capstone_project.Conductor;


import com.bus_season_ticket.capstone_project.User.Role;
import com.bus_season_ticket.capstone_project.User.User;
import com.bus_season_ticket.capstone_project.User.UserRepo;
import com.bus_season_ticket.capstone_project.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConductorService {

    @Autowired
    private  final UserRepo userRepo;
    @Autowired
    private final AuthenticationService authenticationService;

    public UserDetailsResponse checkUser(String userId) {

        UUID id = UUID.fromString(userId);
        Optional<User> user = userRepo.findById(id);
        if(user.isPresent()){
            List<String> userRoles = user.get().getRoles().stream()
                    .map(role -> role.getRole().toString())
                    .collect(Collectors.toList());
            if(userRoles.contains(Role.STUDENT.toString().trim().toUpperCase()) && Objects.equals(user.get().getUserStatusMaintain().getStatus(), "active".trim().toLowerCase())){
                return UserDetailsResponse.builder()
                        .status(true)
                        .role(Role.STUDENT.toString().trim().toUpperCase())
                        .build();
            } else if (userRoles.contains(Role.ADULT.toString().trim().toUpperCase()) && Objects.equals(user.get().getUserStatusMaintain().getStatus(), "active".trim().toLowerCase())) {
                return UserDetailsResponse.builder()
                        .status(true)
                        .role(Role.ADULT.toString().trim().toUpperCase())
                        .build();
            }

        }
            return null;

    }

    public TicketDetailsOfUserResponse getUserTicketDetails(UUID userId) {
        Optional<User> user = userRepo.findById(userId);
        int daysDifference;
        if(user.isPresent()){
            if( user.get().getUserSubscription().getStartDate() == null || user.get().getUserSubscription().getEndDate() == null ){
                daysDifference = 0;
            }else{
                daysDifference = (int) ChronoUnit.DAYS.between(LocalDate.now(), user.get().getUserSubscription().getEndDate() );
            }
           TicketDetailsOfUserResponse ticketDetailsOfUserResponse =TicketDetailsOfUserResponse.builder()
                   .month( user.get().getUserSubscription().getMonth().isEmpty() ? "null" : user.get().getUserSubscription().getMonth())
                   .remainsDays(String.valueOf(daysDifference))
                   .build();
            return ticketDetailsOfUserResponse;

        }else{
            return null;
        }
    }

    public Boolean sendOTP(String userName) {
        Optional<User> user = userRepo.findByUserName(userName);
        if(user.isPresent()){
           String email= user.get().getEmail();
           authenticationService.sendOTP(email);
           return true;
        }else {
            return false;
        }
    }
}
