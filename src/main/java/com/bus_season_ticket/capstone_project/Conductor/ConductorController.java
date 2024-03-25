package com.bus_season_ticket.capstone_project.Conductor;


import com.bus_season_ticket.capstone_project.JourneyMaker.UserJourneyService;
import com.bus_season_ticket.capstone_project.User.User;
import com.bus_season_ticket.capstone_project.User.UserPhotos;
import com.bus_season_ticket.capstone_project.User.UserRepo;
import com.bus_season_ticket.capstone_project.User.UsersService;
import com.bus_season_ticket.capstone_project.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/conductor")
@RequiredArgsConstructor
public class ConductorController {

    @Autowired
    private final ConductorService conductorService;
    @Autowired
    private final UsersService userService;
    @Autowired
    private final UserJourneyService userJourneyService;
    @Autowired
    private final AuthenticationService service;


    @GetMapping("/checkUser/{userId}")
    public ResponseEntity<UserDetailsResponse> checkUser(@PathVariable String userId) {

        UserDetailsResponse userDetailsResponse = conductorService.checkUser(userId);

        if (userDetailsResponse == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(userDetailsResponse);
        }
    }


    @GetMapping("/profilePhoto/{userId}")
    public ResponseEntity<byte[]> getProfilePhoto(@PathVariable String userId) {
        Optional<UserPhotos> userPhotos = userService.getProfilePhoto(userId);

        if (userPhotos.isPresent()) {
            byte[] imageData = userPhotos.get().getData();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Adjust content type based on your image format

            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/student/checkJourney/{userId}/{date}")
    public ResponseEntity<Integer> checkJourney(@PathVariable UUID userId, @PathVariable LocalDate date){
//        UUID userId = UUID.fromString(id);
        try{
            int status = userJourneyService.markAttendanceStudents(userId,date);
            return ResponseEntity.ok(status);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/getName/{userId}")
    public ResponseEntity<String> checkAlreadyUsers(
            @PathVariable UUID userId){
        String userName = service.getName(userId);
        return ResponseEntity.ok(userName);

    }

    @GetMapping("/ticketStatus/{userId}")
    public ResponseEntity<TicketDetailsOfUserResponse> getTicketStatus(
            @PathVariable UUID userId){

        TicketDetailsOfUserResponse ticket = conductorService.getUserTicketDetails(userId);
        if(ticket == null){
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok(ticket);
        }

    }




}
