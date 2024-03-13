package com.bus_season_ticket.capstone_project.Subscription;


import com.bus_season_ticket.capstone_project.User.User;
import com.bus_season_ticket.capstone_project.User.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@CrossOrigin
public class UserSubscriptionService {

    @Autowired
    private final UserRepo userRepo;
    @Autowired
    private final UserSubscriptionRepo userSubscriptionRepo;
    @Transactional
    public boolean checkSubscriptionGetDuration(String userId){
        Optional<User> user = userRepo.findById(UUID.fromString(userId));
        if(user.isPresent()) {
            LocalDate currentDate = LocalDate.now();
            LocalDate lastDayOfMonth = currentDate.with(TemporalAdjusters.lastDayOfMonth()); //the last day of the current month based on the current date.

            if (currentDate.isAfter(lastDayOfMonth.minusWeeks(1)) && currentDate.isBefore(lastDayOfMonth.plusWeeks(1))) {
                if(user.get().getUserSubscription() != null){
                    return !user.get().getUserSubscription().isNextMonthSubscription();
                }
                return false;
            }
        }
            return false;
    }

    public boolean studentPurchaseSubscription(String userId){
        Optional<User> user = userRepo.findById(UUID.fromString(userId));
        LocalDate purchaseDate;
        LocalDate expiredDate;
        if(user.isPresent()){
            LocalDate currentDate = LocalDate.now();

            Month currentMonth = currentDate.getMonth();
            Month nextMonth = currentMonth.plus(1);
            String currentMonthFullName = currentMonth.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            String nextMonthFullName = nextMonth.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            // Calculate the last day of the current month
            LocalDate lastDayOfMonth = currentDate.with(TemporalAdjusters.lastDayOfMonth());
            // Calculate the first day of the last week of the current month
            LocalDate firstDayOfLastWeek = lastDayOfMonth.minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));


            LocalDate firstDayOfMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth());// Get the first day of the current month
            LocalDate lastDayOfFirstWeek = firstDayOfMonth.plusDays(6);// Get the last day of the first week of the current month
            boolean isWithinFirstWeek = currentDate.isBefore(lastDayOfFirstWeek.plusDays(1));// Check if the current date is within the first week of the current month

            // Check if the current date is within the last week of the current month
            if (currentDate.isAfter(firstDayOfLastWeek) && currentDate.isBefore(lastDayOfMonth)) {
                if(user.get().getUserSubscription() != null){
                    if(user.get().getUserSubscription().isNextMonthSubscription()){
                        return false;
                    }else{
                        user.get().getUserSubscription().setNextMonthSubscription(true);
                                userRepo.save(user.get());
                                return true;
                    }
                }else{
                    purchaseDate = LocalDate.now().plusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
                    expiredDate = LocalDate.now().plusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
                    var userSubscription = UserSubscription.builder()
                            .nextMonthSubscription(true)
                            .startDate(purchaseDate)
                            .endDate(expiredDate)
                            .month(nextMonthFullName)
                            .build();
                    user.get().setUserSubscription(userSubscription);
                    userRepo.save(user.get());
                    return true;
                }


            } else if (isWithinFirstWeek) {
                        purchaseDate = LocalDate.now();
                        expiredDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

                        if(user.get().getUserStatusMaintain().getVerified()){
                            return false;
                        }else{
                                var userSubscription = UserSubscription.builder()
                                        .nextMonthSubscription(false)
                                        .startDate(purchaseDate)
                                        .endDate(expiredDate)
                                        .month(currentMonthFullName)
                                        .build();
                                User userSave = user.get();
                                userSave.getUserStatusMaintain().setVerified(true);
                                userSave.setUserSubscription(userSubscription);
                                userRepo.save(userSave);
                                return true;

                        }

                    }
            else{
                purchaseDate = LocalDate.now();
                expiredDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

                if(user.get().getUserStatusMaintain().getVerified()){
                    return false;
                }else{
                    var userSubscription = UserSubscription.builder()
                            .nextMonthSubscription(false)
                            .startDate(purchaseDate)
                            .endDate(expiredDate)
                            .month(currentMonthFullName)
                            .build();
                    User userSave = user.get();
                    userSave.getUserStatusMaintain().setVerified(true);
                    userSave.setUserSubscription(userSubscription);
                    userRepo.save(userSave);
                    return true;

                }
            }

        }else{
                return false;
        }

    }

    public UserRespond getStudentTicketData(String userId){
        Optional<User> user = userRepo.findById(UUID.fromString(userId));
        String expiredDate;
        String purchaseDate;
        int daysDifference;

        if(user.isPresent()){

            boolean checkTimeLine = checkSubscriptionGetDuration(userId);

            if(user.get().getUserSubscription() == null){

                expiredDate = "0000-00-00";

                purchaseDate = "0000-00-00";

                daysDifference = 0;

            }else{
                LocalDate currentDate = LocalDate.now();
                LocalDate lastDayOfMonth = currentDate.with(TemporalAdjusters.lastDayOfMonth());
                LocalDate firstDayOfLastWeek = lastDayOfMonth.minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                if (currentDate.isAfter(firstDayOfLastWeek) && currentDate.isBefore(lastDayOfMonth)) {
                    if(user.get().getUserSubscription().isNextMonthSubscription()){
                        expiredDate = user.get().getUserSubscription().getEndDate().toString();
                        purchaseDate = user.get().getUserSubscription().getStartDate().toString();
                        daysDifference = 0;
                        return UserRespond.builder()
                                .availableDays(daysDifference)
                                .nextMonthPurchase(user.get().getUserSubscription().isNextMonthSubscription())
                                .expiredDate( expiredDate)
                                .purchaseDate( purchaseDate )
                                .amount(user.get().getUserBusDetails().getCharge().toString())
                                .verification(user.get().getUserStatusMaintain().getVerified())
                                .purchaseAvailability(true)
                                .build();
                    }
                }
                if(user.get().getUserSubscription().getEndDate() == null){
                    expiredDate = "0000-00-00";
                }else{
                    expiredDate = user.get().getUserSubscription().getEndDate().toString();
                }
                if( user.get().getUserSubscription().getStartDate() == null){
                    purchaseDate = "0000-00-00";
                }else{
                    purchaseDate = user.get().getUserSubscription().getStartDate().toString();
                }

                if( user.get().getUserSubscription().getStartDate() == null || user.get().getUserSubscription().getEndDate() == null ){
                    daysDifference = 0;
                }else{
                    daysDifference = (int) ChronoUnit.DAYS.between(LocalDate.now(), user.get().getUserSubscription().getEndDate() );
                }
            }
            return UserRespond.builder()
                    .availableDays(daysDifference)
                    .expiredDate( expiredDate)
                    .purchaseDate( purchaseDate )
                    .amount(user.get().getUserBusDetails().getCharge().toString())
                    .verification(user.get().getUserStatusMaintain().getVerified())
                    .nextMonthPurchase(user.get().getUserSubscription().isNextMonthSubscription())
                    .purchaseAvailability(checkTimeLine)
                    .build();

        }
        return null;

    }

    public boolean checkPurchases(String userId) {
        UUID id = UUID.fromString(userId);
        Optional<User> user = userRepo.findById(id);

        if(user.isPresent()){
            LocalDate currentDate = LocalDate.now();

            Month currentMonth = currentDate.getMonth();
            Month nextMonth = currentMonth.plus(1);
            String currentMonthFullName = currentMonth.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            String nextMonthFullName = nextMonth.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            // Calculate the last day of the current month
            LocalDate lastDayOfMonth = currentDate.with(TemporalAdjusters.lastDayOfMonth());
            // Calculate the first day of the last week of the current month
            LocalDate firstDayOfLastWeek = lastDayOfMonth.minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

            LocalDate firstDayOfMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth());// Get the first day of the current month
            LocalDate lastDayOfFirstWeek = firstDayOfMonth.plusDays(6);// Get the last day of the first week of the current month
            boolean isWithinFirstWeek = currentDate.isBefore(lastDayOfFirstWeek.plusDays(1));// Check if the current date is within the first week of the current month

            // Check if the current date is within the last week of the current month
            if (currentDate.isAfter(firstDayOfLastWeek) && currentDate.isBefore(lastDayOfMonth) || isWithinFirstWeek) {
                if(currentDate.isAfter(firstDayOfLastWeek) && currentDate.isBefore(lastDayOfMonth)){
                    if(user.get().getUserSubscription()!= null){
                        if(user.get().getUserSubscription().isNextMonthSubscription()){
                            return false;
                        }else{
                            return true;
                        }
                    }else{
                        return true;
                    }

                }else {
                    if(user.get().getUserStatusMaintain().getVerified()){
                        return false;
                    }{
                        return true;
                    }
                }

            }else{
                return false;
            }

        }else{
            return false;
        }
    }
}
