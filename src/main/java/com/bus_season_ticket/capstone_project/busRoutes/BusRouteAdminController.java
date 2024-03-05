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
    public ResponseEntity<?> register(@RequestBody BusRouteRequest busRoute) {
        try {
            if(busService.setRoute(busRoute)){
                return ResponseEntity.status(HttpStatus.OK).build();
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRoute(@RequestBody BusRouteRequest busRoute, @PathVariable int id) {
        try {
            boolean status = busService.updateRoute(busRoute,id);
                    if(status){
                        return ResponseEntity.status(HttpStatus.OK).build();
                    }else{
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                    }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@RequestBody DeleteBusRouteRequest busRoute, @PathVariable int id) {
        try {
            boolean status = busService.deleteRoute(busRoute,id);
            if(status){
                return ResponseEntity.status(HttpStatus.OK).build();
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/routeDeleteList")
    public ResponseEntity<List<DeleteRouteResponse>> getDeleteList() {
        try {
            List<DeleteRouteResponse> allBusRoutes = busService.getDeleteRouteList();
            return ResponseEntity.ok(allBusRoutes);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/routeList")
    public ResponseEntity<List<BusRouteResponse>> getList() {
        try {
            List<BusRouteResponse> allBusRoutes = busService.getRouteByAdmin();
            return ResponseEntity.ok(allBusRoutes);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/singleRoute/{id}")
    public ResponseEntity<BusRouteResponse> getOneRoute(@PathVariable int id) {
        try {
            BusRouteResponse allBusRoutes = busService.getOneRouteByAdmin(id);
            return ResponseEntity.ok(allBusRoutes);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
