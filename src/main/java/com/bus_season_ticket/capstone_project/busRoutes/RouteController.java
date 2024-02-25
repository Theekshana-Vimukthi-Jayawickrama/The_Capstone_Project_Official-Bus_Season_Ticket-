package com.bus_season_ticket.capstone_project.busRoutes;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/route")
@RequiredArgsConstructor
public class RouteController {

    private final BusRouteService busService;

    @GetMapping("/getStudentCharge/{route}")
    public ResponseEntity<Double> getStudentCharge(@PathVariable String route){
        try{
            Double charge = busService.calculateChargeStudent(route);
            return  ResponseEntity.ok(charge);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/getAdultCharge/{route}")
    public ResponseEntity<Double> getAdultCharge(
            @PathVariable String route,
            @RequestBody Map<String, Boolean> selectedDays
    ) {
        try {
            Double charge = busService.calculateChargeAdult(route, selectedDays);
            return ResponseEntity.ok(charge);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/routeList")
    public ResponseEntity<List<String>> getList() {
        try {
            List<String> allBusRoutes = busService.getRoute();
            return ResponseEntity.ok(allBusRoutes);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

}
