package com.bus_season_ticket.capstone_project.JourneyMaker;


import com.bus_season_ticket.capstone_project.User.Role;
import com.bus_season_ticket.capstone_project.User.User;
import com.bus_season_ticket.capstone_project.User.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class DailyJourneyUpdater {

    @Autowired
    private UserJourneyService userJourneyService;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private UserJourneyRepository userJourneyRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Runs daily at midnight
//    @Scheduled(fixedRate = 1000)
    public void updateJourneyCounts() {
        // Get yesterday's date
        LocalDate yesterday = LocalDate.now().minusDays(1);

        // Get all users (You might have a different method to retrieve users)
        List<User> users = userRepository.findAll();

        for (User user : users) {
            // Check if the user had a journey yesterday
            if(user.getUserStatusMaintain().getVerified().equals(true)){
                boolean hasJourneyYesterday = checkJourneyForUserYesterday(user.getId(), yesterday);

                List<String> userRoles = user.getRoles().stream()
                        .map(role -> role.getRole().toString())
                        .collect(Collectors.toList());

                if(!hasJourneyYesterday){
                    if(userRoles.contains(Role.STUDENT.toString())){
                        String email = user.getEmail();
                        userJourneyService.updateStudentJourney( yesterday, false, email);
                    }else if(userRoles.contains(Role.ADULT.toString())){
                        String email = user.getEmail();
                        userJourneyService.checkDays( yesterday, false, email);
                    }
                }
            }
            // Update the journey count for the user based on yesterday's journey

        }
    }

    private boolean checkJourneyForUserYesterday(UUID userId, LocalDate yesterday) {
        // Query the database using UserJourneyRepository to check if there's a journey for the user on yesterday's date
        Optional<UserJourney> userJourneyOptional = userJourneyRepository.findByUserIdAndJourneyDate(userId, yesterday);

        return userJourneyOptional.isPresent(); // Return true if a journey exists for the user on the specified date
    }
}

