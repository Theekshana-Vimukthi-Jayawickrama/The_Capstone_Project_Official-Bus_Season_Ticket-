package com.bus_season_ticket.capstone_project.JourneyMaker;


import com.bus_season_ticket.capstone_project.User.Role;
import com.bus_season_ticket.capstone_project.User.User;
import com.bus_season_ticket.capstone_project.User.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserJourneyService {
    @Autowired
    private UserJourneyRepository userJourneyRepository;

    @Autowired
    private UserRepo userRepo;

    public String checkDays(LocalDate date, boolean hasJourney, String id){
        try {
            UUID userId = UUID.fromString(id);
            Optional<User> userOptional = userRepo.findById(userId);
            DayOfWeek dayOfWeek = date.getDayOfWeek();

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                String userEmail = user.getEmail();

                switch (dayOfWeek) {
                    case MONDAY:
                        if(userOptional.get().getSelectDays().isMonday()){
                            return updateStudentJourney(date,hasJourney,id);

                    }else{
                            int journeyCount = 500;
                            List<UserJourney>userJourneys = userJourneyRepository.findByUserAndJourneyDate(user,date);
                            if(userJourneys.isEmpty()){
                                UserJourney userJourney = UserJourney.builder()
                                        .user(user)
                                        .email(userEmail)
                                        .userId(userId)
                                        .journeyDate(date)
                                        .journeyCount(500)
                                        .build();

                                user.getJourneys().add(userJourney);
                                userRepo.save(user);
                            }

                            return "This day not available for user";
                        }
                    case TUESDAY:
                        if(userOptional.get().getSelectDays().isTuesday()){
                            return updateStudentJourney(date,hasJourney,id);

                        }else{
                            int journeyCount = 500;
                            List<UserJourney>userJourneys = userJourneyRepository.findByUserAndJourneyDate(user,date);
                            if(userJourneys.isEmpty()){
                                UserJourney userJourney = UserJourney.builder()
                                        .user(user)
                                        .email(userEmail)
                                        .userId(userId)
                                        .journeyDate(date)
                                        .journeyCount(500)
                                        .build();

                                user.getJourneys().add(userJourney);
                                userRepo.save(user);
                            }
                            return "This day not available for user";
                        }
                    case WEDNESDAY:
                        if(userOptional.get().getSelectDays().isWednesday()){
                            return updateStudentJourney(date,hasJourney,id);

                        }else{
                            int journeyCount = 500;
                            List<UserJourney>userJourneys = userJourneyRepository.findByUserAndJourneyDate(user,date);
                            if(userJourneys.isEmpty()){
                                UserJourney userJourney = UserJourney.builder()
                                        .user(user)
                                        .email(userEmail)
                                        .userId(userId)
                                        .journeyDate(date)
                                        .journeyCount(500)
                                        .build();

                                user.getJourneys().add(userJourney);
                                userRepo.save(user);
                            }
                            return "This day not available for user";
                        }
                    case THURSDAY:
                        if(userOptional.get().getSelectDays().isThursday()){
                            return updateStudentJourney(date,hasJourney,id);

                        }else{
                            int journeyCount = 500;
                            List<UserJourney>userJourneys = userJourneyRepository.findByUserAndJourneyDate(user,date);
                            if(userJourneys.isEmpty()){
                                UserJourney userJourney = UserJourney.builder()
                                        .user(user)
                                        .email(userEmail)
                                        .userId(userId)
                                        .journeyDate(date)
                                        .journeyCount(500)
                                        .build();

                                user.getJourneys().add(userJourney);
                                userRepo.save(user);
                            }
                            return "This day not available for user";
                        }
                    case FRIDAY:
                        if(userOptional.get().getSelectDays().isFriday()){

                            return updateStudentJourney(date,hasJourney,id);

                        }else{
                            int journeyCount = 500;
                            List<UserJourney>userJourneys = userJourneyRepository.findByUserAndJourneyDate(user,date);
                            if(userJourneys.isEmpty()){
                                UserJourney userJourney = UserJourney.builder()
                                        .user(user)
                                        .email(userEmail)
                                        .userId(userId)
                                        .journeyDate(date)
                                        .journeyCount(500)
                                        .build();

                                user.getJourneys().add(userJourney);
                                userRepo.save(user);
                            }
                            return "This day not available for user";
                        }
                    case SATURDAY:
                        if(userOptional.get().getSelectDays().isSaturday()){
                            return updateStudentJourney(date,hasJourney,id);

                        }else{
                            int journeyCount = 500;
                            List<UserJourney>userJourneys = userJourneyRepository.findByUserAndJourneyDate(user,date);
                            if(userJourneys.isEmpty()){
                                UserJourney userJourney = UserJourney.builder()
                                        .user(user)
                                        .email(userEmail)
                                        .userId(userId)
                                        .journeyDate(date)
                                        .journeyCount(500)
                                        .build();

                                user.getJourneys().add(userJourney);
                                userRepo.save(user);
                            }
                            return "This day not available for user";
                        }
                    case SUNDAY:
                        if(userOptional.get().getSelectDays().isSunday()){
                            return updateStudentJourney(date,hasJourney,id);

                        }else{
                            int journeyCount = 500;
                            List<UserJourney>userJourneys = userJourneyRepository.findByUserAndJourneyDate(user,date);
                            if(userJourneys.isEmpty()){
                                UserJourney userJourney = UserJourney.builder()
                                        .user(user)
                                        .email(userEmail)
                                        .userId(userId)
                                        .journeyDate(date)
                                        .journeyCount(500)
                                        .build();

                                user.getJourneys().add(userJourney);
                                userRepo.save(user);
                            }
                            return "This day not available for user";
                        }
                    default:
                        return "This input is invalid.";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "There is no a user.";
    }

    public String updateStudentJourney(LocalDate date, boolean hasJourney, String id) {
        try {
            UUID userId = UUID.fromString(id);
            Optional<User> userOptional = userRepo.findById(userId);

            if (userOptional.isPresent()) {
                if(userOptional.get().getUserStatusMaintain().getVerified()){
                    User user = userOptional.get();
                    String userEmail = user.getEmail();

                    Optional<UserJourney> userJourneyOptional = userJourneyRepository.findByUserIdAndJourneyDate(userId, date);

                    if (userJourneyOptional.isPresent()) {
                        if(userJourneyOptional.get().getJourneyCount() == 2){
                            return "All journeys have been finished.";
                        }else{
                            UserJourney userJourney = userJourneyOptional.get();
                            userJourney.setJourneyCount(2);
                            userJourneyRepository.save(userJourney);
                            return "updated";
                        }
                    } else {
                        UserJourney userJourney = UserJourney.builder()
                                .user(user)
                                .email(userEmail)
                                .userId(userId)
                                .journeyDate(date)
                                .journeyCount(hasJourney ? 1 : 0)
                                .build();
                        user.getJourneys().add(userJourney);
                        userRepo.save(user);
                        return "updated";

                    }
                }else{
                    return ("Not verified");
                }

            } else {
                return ("User not found" );
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e);
        }
            return "error";
    }

    public int markAttendanceStudents(UUID userId, LocalDate date){

        Optional<UserJourney> userJourneyOptional = userJourneyRepository.findByUserIdAndJourneyDate(userId, date);

        if(userJourneyOptional.isPresent()){
            if(userJourneyOptional.get().getJourneyCount() == 1){
                return 1;
            }else if(userJourneyOptional.get().getJourneyCount() == 2) {
                return 2;
            } else if (userJourneyOptional.get().getJourneyCount() == 0) {
                return 0;
            }
        }
           return 404;
    }

    public int markAttendance(UUID userId){
        LocalDate date = LocalDate.now();

        Optional<UserJourney> userJourneyOptional = userJourneyRepository.findByUserIdAndJourneyDate(userId, date);

        if(userJourneyOptional.isPresent()){
            if(userJourneyOptional.get().getJourneyCount() == 1){
                return 1;
            }else if(userJourneyOptional.get().getJourneyCount() == 2) {
                return 2;
            } else if (userJourneyOptional.get().getJourneyCount() == 0) {
                return 0;
            }
        }
        return 0;
    }

    public RouteDaysSelectionResponse getSelectedDaysByAdult(UUID userId) {
        Optional<User> user = userRepo.findById(userId);

        List<String> userRoles = user.get().getRoles().stream()
                .map(role -> role.getRole().toString())
                .collect(Collectors.toList());

        if(user.isPresent() && userRoles.contains(Role.ADULT.toString()) ){
            boolean monday = user.get().getSelectDays().isMonday();
            boolean tuesday = user.get().getSelectDays().isTuesday();
            boolean wednesday = user.get().getSelectDays().isWednesday();
            boolean thursday = user.get().getSelectDays().isThursday();
            boolean friday = user.get().getSelectDays().isFriday();
            boolean saturday = user.get().getSelectDays().isSaturday();
            boolean sunday = user.get().getSelectDays().isSunday();
            return RouteDaysSelectionResponse.builder()
                    .monday(monday)
                    .tuesday(tuesday)
                    .wednesday(wednesday)
                    .thursday(thursday)
                    .friday(friday)
                    .saturday(saturday)
                    .sunday(sunday)
                    .build();

        }else{
            return null;
        }
    }

    public String checkDaysOFAdult(LocalDate date, boolean hasJourney, String id, UUID conductorId) {
        try {
            UUID userId = UUID.fromString(id);
            Optional<User> userOptional = userRepo.findById(userId);
            DayOfWeek dayOfWeek = date.getDayOfWeek();

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                String userEmail = user.getEmail();

                switch (dayOfWeek) {
                    case MONDAY:
                        if(userOptional.get().getSelectDays().isMonday()){
                            return updateUserJourney(date,hasJourney,id, conductorId);

                        }else{
                            int journeyCount = 500;
                            List<UserJourney>userJourneys = userJourneyRepository.findByUserAndJourneyDate(user,date);
                            if(userJourneys.isEmpty()){
                                UserJourney userJourney = UserJourney.builder()
                                        .user(user)
                                        .email(userEmail)
                                        .userId(userId)
                                        .journeyDate(date)
                                        .journeyCount(500)
                                        .build();

                                user.getJourneys().add(userJourney);
                                userRepo.save(user);
                            }

                            return "This day not available for user";
                        }
                    case TUESDAY:
                        if(userOptional.get().getSelectDays().isTuesday()){
                            return updateUserJourney(date,hasJourney,id, conductorId);

                        }else{
                            int journeyCount = 500;
                            List<UserJourney>userJourneys = userJourneyRepository.findByUserAndJourneyDate(user,date);
                            if(userJourneys.isEmpty()){
                                UserJourney userJourney = UserJourney.builder()
                                        .user(user)
                                        .email(userEmail)
                                        .userId(userId)
                                        .journeyDate(date)
                                        .journeyCount(500)
                                        .build();

                                user.getJourneys().add(userJourney);
                                userRepo.save(user);
                            }
                            return "This day not available for user";
                        }
                    case WEDNESDAY:
                        if(userOptional.get().getSelectDays().isWednesday()){
                            return updateUserJourney(date,hasJourney,id, conductorId);

                        }else{
                            int journeyCount = 500;
                            List<UserJourney>userJourneys = userJourneyRepository.findByUserAndJourneyDate(user,date);
                            if(userJourneys.isEmpty()){
                                UserJourney userJourney = UserJourney.builder()
                                        .user(user)
                                        .email(userEmail)
                                        .userId(userId)
                                        .journeyDate(date)
                                        .journeyCount(500)
                                        .build();

                                user.getJourneys().add(userJourney);
                                userRepo.save(user);
                            }
                            return "This day not available for user";
                        }
                    case THURSDAY:
                        if(userOptional.get().getSelectDays().isThursday()){
                            return updateUserJourney(date,hasJourney,id, conductorId);

                        }else{
                            int journeyCount = 500;
                            List<UserJourney>userJourneys = userJourneyRepository.findByUserAndJourneyDate(user,date);
                            if(userJourneys.isEmpty()){
                                UserJourney userJourney = UserJourney.builder()
                                        .user(user)
                                        .email(userEmail)
                                        .userId(userId)
                                        .journeyDate(date)
                                        .journeyCount(500)
                                        .build();

                                user.getJourneys().add(userJourney);
                                userRepo.save(user);
                            }
                            return "This day not available for user";
                        }
                    case FRIDAY:
                        if(userOptional.get().getSelectDays().isFriday()){

                            return updateUserJourney(date,hasJourney,id, conductorId);

                        }else{
                            int journeyCount = 500;
                            List<UserJourney>userJourneys = userJourneyRepository.findByUserAndJourneyDate(user,date);
                            if(userJourneys.isEmpty()){
                                UserJourney userJourney = UserJourney.builder()
                                        .user(user)
                                        .email(userEmail)
                                        .userId(userId)
                                        .journeyDate(date)
                                        .journeyCount(500)
                                        .build();

                                user.getJourneys().add(userJourney);
                                userRepo.save(user);
                            }
                            return "This day not available for user";
                        }
                    case SATURDAY:
                        if(userOptional.get().getSelectDays().isSaturday()){
                            return updateUserJourney(date,hasJourney,id, conductorId);

                        }else{
                            int journeyCount = 500;
                            List<UserJourney>userJourneys = userJourneyRepository.findByUserAndJourneyDate(user,date);
                            if(userJourneys.isEmpty()){
                                UserJourney userJourney = UserJourney.builder()
                                        .user(user)
                                        .email(userEmail)
                                        .userId(userId)
                                        .journeyDate(date)
                                        .journeyCount(500)
                                        .build();

                                user.getJourneys().add(userJourney);
                                userRepo.save(user);
                            }
                            return "This day not available for user";
                        }
                    case SUNDAY:
                        if(userOptional.get().getSelectDays().isSunday()){
                            return updateUserJourney(date,hasJourney,id, conductorId);

                        }else{
                            int journeyCount = 500;
                            List<UserJourney>userJourneys = userJourneyRepository.findByUserAndJourneyDate(user,date);
                            if(userJourneys.isEmpty()){
                                UserJourney userJourney = UserJourney.builder()
                                        .user(user)
                                        .email(userEmail)
                                        .userId(userId)
                                        .journeyDate(date)
                                        .journeyCount(500)
                                        .build();

                                user.getJourneys().add(userJourney);
                                userRepo.save(user);
                            }
                            return "This day not available for user";
                        }
                    default:
                        return "This input is invalid.";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "There is no a user.";

    }

    public String updateUserJourney(LocalDate date, boolean hasJourney, String id, UUID conductorId) {
        try {
            UUID userId = UUID.fromString(id);
            Optional<User> userOptional = userRepo.findById(userId);

            if (userOptional.isPresent()) {
                if(userOptional.get().getUserStatusMaintain().getVerified()){
                    User user = userOptional.get();
                    String userEmail = user.getEmail();

                    Optional<UserJourney> userJourneyOptional = userJourneyRepository.findByUserIdAndJourneyDate(userId, date);

                    if (userJourneyOptional.isPresent()) {
                        if(userJourneyOptional.get().getJourneyCount() == 2){
                            return "All journeys have been finished.";
                        } else if (userJourneyOptional.get().getJourneyCount() == 0) {
                            UserJourney userJourney = userJourneyOptional.get();
                            userJourney.setJourneyCount(1);
                            userJourneyRepository.save(userJourney);
                            return "updated";
                        } else if(userJourneyOptional.get().getJourneyCount() == 1){
                            UserJourney userJourney = userJourneyOptional.get();
                            userJourney.setJourneyCount(2);
                            userJourneyRepository.save(userJourney);
                            return "updated";
                        }
                    } else {

                        UserJourney userJourney = UserJourney.builder()
                                .user(user)
                                .email(userEmail)
                                .userId(userId)
                                .journeyDate(date)
                                .journeyCount(hasJourney ? 1 : 0)
                                .build();
                        user.getJourneys().add(userJourney);
                        userRepo.save(user);
                        return "updated";

                    }
                }else{
                    return ("Not verified");
                }

            } else {
                return ("User not found" );
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e);
        }
        return "error";
    }
}

