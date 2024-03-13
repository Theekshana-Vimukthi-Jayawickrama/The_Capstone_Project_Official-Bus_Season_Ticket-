package com.bus_season_ticket.capstone_project.Subscription;


import com.bus_season_ticket.capstone_project.User.User;
import com.bus_season_ticket.capstone_project.User.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;

@Component
public class UserSubscriptionDisable {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserSubscriptionRepo studentSubscriptionRepo;

    @Scheduled(cron = "0 0 0 L * ?") // Execute at midnight on the last day of the month
//    @Scheduled(fixedRate = 1000)
    public void executeMonthlyTask() {
        List<User> users = userRepo.findAll();
        for (User user : users) {

                if ( user.getUserSubscription().isNextMonthSubscription()) {
                    LocalDate purchaseDate = LocalDate.now();
                    LocalDate expiredDate = LocalDate.now().plusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
                    Month currentMonth = expiredDate.getMonth();
                    String currentMonthFullName = currentMonth.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
                    user.getUserStatusMaintain().setVerified(true);
                    user.getUserSubscription().setNextMonthSubscription(false);
                    user.getUserSubscription().setStartDate(purchaseDate);
                    user.getUserSubscription().setEndDate(expiredDate);
                    user.getUserSubscription().setMonth(currentMonthFullName);
                        userRepo.save(user);
                } else {
                    user.getUserStatusMaintain().setVerified(false);
                    userRepo.save(user);
                }
        }
    }
}
