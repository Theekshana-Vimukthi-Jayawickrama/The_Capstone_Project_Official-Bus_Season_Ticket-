package com.bus_season_ticket.capstone_project.busRoutes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@CrossOrigin
@RequiredArgsConstructor
public class BusRouteService {

    private final BusRouteRepository busRouteRepository;

    public void setRoute(BusRouteRequest request){
        BusRoute busRoute = BusRoute.builder()
                .route(request.getRoute())
                .routeNo(request.getRouteNo())
                .distance(request.getDistance())
                .perDayCharge(request.getPerDayCharge())
                .build();
        busRouteRepository.save(busRoute);
    }

    public List<String> getRoute(){
        List<BusRoute> busRoutes = busRouteRepository.findAll();

        List<String> allBusRoute = new ArrayList<>();

        for(int i=0; i < busRoutes.size() ;i++){
            allBusRoute.add(busRoutes.get(i).getRoute());
        }
        return allBusRoute;
    }

    public Double calculateChargeStudent(String route){
        BusRoute busRoutes = busRouteRepository.findByRoute(route);

        LocalDate today = LocalDate.now();
        //TemporalAdjusters.lastDayOfMonth() is used in combination with the with() method of LocalDate to obtain the last day of the current month.
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        //ChronoUnit.DAYS.between(date1, date2) calculates the number of days between date1 and date2
        long daysBetween = ChronoUnit.DAYS.between(today, endOfMonth);

        if(busRoutes.getRoute().equals(route)){
            Double charge = busRoutes.getPerDayCharge();
            Double studentCharge = ((charge *40) / 100) * daysBetween;
            return studentCharge;
        }
        return null;
    }
    public Double calculateChargeAdult(String route, Map<String, Boolean> selectedDays){
        BusRoute busRoutes = busRouteRepository.findByRoute(route);

        LocalDate today = LocalDate.now();
        //TemporalAdjusters.lastDayOfMonth() is used in combination with the with() method of LocalDate to obtain the last day of the current month.
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        //ChronoUnit.DAYS.between(date1, date2) calculates the number of days between date1 and date2
        long daysBetween = ChronoUnit.DAYS.between(today, endOfMonth);

        int[] dayCounts = new int[7]; // Array to store counts for each day of the week

        for (int i = 0; i <= daysBetween; i++) {
            LocalDate currentDate = today.plusDays(i);
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek(); //obtain the day of the week for a given LocalDate
            int dayIndex = dayOfWeek.getValue() - 1; // DayOfWeek starts from 1 (Monday) but array index starts from 0
            dayCounts[dayIndex]++; // Increment count for the corresponding day of the week
        }
            int totalDayCount = 0;
        if (selectedDays.get("Monday").equals(true)){
            totalDayCount = totalDayCount + dayCounts[0];
        }
        if (selectedDays.get("Tuesday").equals(true)){
            totalDayCount =  totalDayCount +dayCounts[1];
        }
        if (selectedDays.get("Wednesday").equals(true)){
            totalDayCount =totalDayCount + dayCounts[2];
        }
        if (selectedDays.get("Thursday").equals(true)){
            totalDayCount =totalDayCount + dayCounts[3];
        }
        if (selectedDays.get("Friday").equals(true)){
            totalDayCount =totalDayCount + dayCounts[4];
        }
        if (selectedDays.get("Saturday").equals(true)){
            totalDayCount =totalDayCount + dayCounts[5];
        }
        if (selectedDays.get("Sunday").equals(true)){
            totalDayCount = totalDayCount + dayCounts[6];
        }

        // Print the counts for each day of the week
        System.out.println("Counts by day of the week:");
        System.out.println("Monday: " + dayCounts[0]);
        System.out.println("Tuesday: " + dayCounts[1]);
        System.out.println("Wednesday: " + dayCounts[2]);
        System.out.println("Thursday: " + dayCounts[3]);
        System.out.println("Friday: " + dayCounts[4]);
        System.out.println("Saturday: " + dayCounts[5]);
        System.out.println("Sunday: " + dayCounts[6]);
        System.out.println("total count" + totalDayCount);

        if(busRoutes.getRoute().equals(route)){
            Double charge = busRoutes.getPerDayCharge();
            return ((charge *60 / 100)  * totalDayCount);
        }
        return null;
    }
}
