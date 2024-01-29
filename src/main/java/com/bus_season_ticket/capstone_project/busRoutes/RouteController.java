package com.bus_season_ticket.capstone_project.busRoutes;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/getAdultCharge/{route}")
    public ResponseEntity<Double> getAdultCharge(@PathVariable String route){
        try{
            Double charge = busService.calculateChargeAdult(route);
            return  ResponseEntity.ok(charge);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

}
