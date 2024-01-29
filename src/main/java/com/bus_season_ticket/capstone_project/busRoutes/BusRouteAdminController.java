package com.bus_season_ticket.capstone_project.busRoutes;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/adminRoute")
@RequiredArgsConstructor
public class BusRouteAdminController {
    private final BusRouteService busService;
    @PostMapping()
    public ResponseEntity<String> register(@RequestBody BusRouteRequest busRoute) {
        try {
            busService.setRoute(busRoute);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
