package com.bus_season_ticket.capstone_project.busRoutes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

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
    public Double calculateChargeAdult(String route){
        BusRoute busRoutes = busRouteRepository.findByRoute(route);

        LocalDate today = LocalDate.now();
        //TemporalAdjusters.lastDayOfMonth() is used in combination with the with() method of LocalDate to obtain the last day of the current month.
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        //ChronoUnit.DAYS.between(date1, date2) calculates the number of days between date1 and date2
        long daysBetween = ChronoUnit.DAYS.between(today, endOfMonth);

        if(busRoutes.getRoute().equals(route)){
            Double charge = busRoutes.getPerDayCharge();
            Double adultCharge = ((charge *60) / 100) * daysBetween;
            return adultCharge;
        }
        return null;
    }
}
