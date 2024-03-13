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
import java.util.Objects;

@Service
@CrossOrigin
@RequiredArgsConstructor
public class BusRouteService {

    private final BusRouteRepository busRouteRepository;
    private final DeleteRouteRepository deleteRouteRepository;
    public boolean setRoute(BusRouteRequest request){
        if(!request.getRouteSource().trim().equalsIgnoreCase(request.getRouteDistance().trim())){
            String route = request.getRouteSource().trim().toUpperCase() +" - "+ request.getRouteDistance().trim().toUpperCase();
            String route1 = request.getRouteDistance().trim()+" - "+ request.getRouteSource().trim();
            List<BusRoute> busRoutes = busRouteRepository.findAll();
            boolean status = true;

            for(int i=0; i < busRoutes.size() ;i++){
                if(busRoutes.get(i).getRoute().equals(route.trim()) || busRoutes.get(i).getRoute().equals(route1.trim())){
                    status = false;
                    break;
                }
            }
            if(status){
                BusRoute busRoute = BusRoute.builder()
                        .route(route.trim())
                        .routeSource(request.getRouteSource())
                        .routeDistance(request.getRouteDistance())
                        .routeNo(request.getRouteNo().toUpperCase().trim())
                        .distance(request.getDistance().toUpperCase().trim())
                        .perDayCharge(request.getPerDayCharge())
                        .build();
                busRouteRepository.save(busRoute);
                return true;
            }

        }else{
            return false;
        }

        return false;
    }

    public List<String> getRoute(){
        List<BusRoute> busRoutes = busRouteRepository.findAll();

        List<String> allBusRoute = new ArrayList<>();

        for(int i=0; i < busRoutes.size() ;i++){
            allBusRoute.add(busRoutes.get(i).getRoute());
        }
        return allBusRoute;
    }

    public List<BusRouteResponse> getRouteByAdmin(){
        List<BusRoute> busRoutes = busRouteRepository.findAll();

        List<BusRouteResponse> allBusRoute = new ArrayList<>();

        for(int i=0; i < busRoutes.size() ;i++){
            BusRouteResponse busRouteRequest = BusRouteResponse.builder()
                    .id(busRoutes.get(i).getId())
                    .routeSource(busRoutes.get(i).getRouteSource())
                    .routeDistance(busRoutes.get(i).getRouteDistance())
                    .distance(busRoutes.get(i).getDistance())
                    .perDayCharge(busRoutes.get(i).getPerDayCharge())
                    .routeNo(busRoutes.get(i).getRouteNo())
                    .build();
            allBusRoute.add(busRouteRequest);
        }
        return allBusRoute;
    }

    public List<DeleteRouteResponse> getDeleteRouteList(){
        List<DeleteRoute> busRoutes = deleteRouteRepository.findAll();

        List<DeleteRouteResponse> allBusRoute = new ArrayList<>();

        for (DeleteRoute busRoute : busRoutes) {
            DeleteRouteResponse busRouteRequest = DeleteRouteResponse.builder()
                    .adminId(busRoute.getAdminId())
                    .route(busRoute.getRoute())
                    .reason(busRoute.getReason())
                    .date(busRoute.getDate())
                    .build();
            allBusRoute.add(busRouteRequest);
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

    public boolean updateRoute(BusRouteRequest request, int id) {
        BusRoute busRoute1 = busRouteRepository.findById(id);

      if(!request.getRouteSource().trim().equalsIgnoreCase(request.getRouteDistance().trim()) ){
        String route = request.getRouteSource().trim().toUpperCase() +" - "+ request.getRouteDistance().trim().toUpperCase();
          String route1 = request.getRouteDistance().trim()+" - "+ request.getRouteSource().trim();
          List<BusRoute> busRoutes = busRouteRepository.findAll();
          boolean status = true;

          for(int i=0; i < busRoutes.size() ;i++){
              if(busRoutes.get(i).getRoute().equals(route.trim()) || busRoutes.get(i).getRoute().equals(route1.trim())){
                  status = false;
                  break;
              }
          }
          if(status){
            busRoute1.setRoute(route.trim());
            busRoute1.setRouteDistance(request.getRouteDistance() );
            busRoute1.setDistance(request.getDistance()+" " +"KM");
            busRoute1.setRouteNo(request.getRouteNo());
            busRoute1.setRouteSource(request.getRouteSource().toUpperCase().trim());
            busRoute1.setPerDayCharge(request.getPerDayCharge());
            busRouteRepository.save(busRoute1);
            return true;
          }else{
              return false;
          }

      }else{
          return false;
      }

    }

    public boolean deleteRoute(DeleteBusRouteRequest busRoute, int id) {
        BusRoute busRoute1 = busRouteRepository.findById(id);
        if(busRoute1 != null){
            DeleteRoute deleteRoute = DeleteRoute.builder()
                    .adminId(busRoute.getAdminId())
                    .reason(busRoute.getReason())
                    .route(busRoute1.getRoute())
                    .date(LocalDate.now())
                    .build();
            deleteRouteRepository.save(deleteRoute);
            busRouteRepository.delete(busRoute1);
            return true;
        }return false;
    }

    public BusRouteResponse getOneRouteByAdmin(int id) {
        BusRoute busRoutes = busRouteRepository.findById(id);

        if(busRoutes != null){
            BusRouteResponse busRouteRequest = BusRouteResponse.builder()
                    .id(busRoutes.getId())
                    .routeSource(busRoutes.getRouteSource())
                    .routeDistance(busRoutes.getRouteDistance())
                    .distance(busRoutes.getDistance())
                    .perDayCharge(busRoutes.getPerDayCharge())
                    .routeNo(busRoutes.getRouteNo())
                    .build();
            return busRouteRequest;
        }

    return null;


    }
}
