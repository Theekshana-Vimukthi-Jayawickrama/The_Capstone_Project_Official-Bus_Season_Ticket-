package com.bus_season_ticket.capstone_project.Deport;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/depot")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DepotController {

    private final DepotService depotService;

    @GetMapping("/getAllDepots")
    public ResponseEntity<List<AddDepotRequest>> getAllSchoolDistricts (){

        try {
            List<AddDepotRequest> depots = depotService.getAllDepotName();

            if(depots == null){
                return ResponseEntity.notFound().build();
            }else{
                return ResponseEntity.ok(depots);
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
