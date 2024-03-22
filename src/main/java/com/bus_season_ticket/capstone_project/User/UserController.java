package com.bus_season_ticket.capstone_project.User;


import com.bus_season_ticket.capstone_project.JourneyMaker.RouteDaysSelectionResponse;
import com.bus_season_ticket.capstone_project.JourneyMaker.UserJourneyService;
import com.bus_season_ticket.capstone_project.Subscription.UserSubscriptionService;
import com.bus_season_ticket.capstone_project.auth.AuthenticationService;
import com.bus_season_ticket.capstone_project.auth.RouteRequest;
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
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final AuthenticationService service;
    @Autowired
    private final UsersService userService;
    @Autowired
    private UserJourneyService userJourneyService;
    @Autowired
    private UserSubscriptionService userSubscriptionService;

    @GetMapping("/getName/{userId}")
    public ResponseEntity<String> checkAlreadyUsers(
            @PathVariable UUID userId){
        String userName = service.getName(userId);
        return ResponseEntity.ok(userName);

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

    @GetMapping("/getRouteDetails/{userId}")
    public ResponseEntity<Optional<RouteResponse>> getRouteDetails(@PathVariable String userId) {
        Optional<RouteResponse> userRoute = userService.getRouteDetails(userId);

        if(userRoute.isPresent()){
            return ResponseEntity.ok(userRoute);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/subscription/route/{userId}")
    public ResponseEntity<Boolean> updateRoutes(@PathVariable String userId, @RequestBody RouteRequest updateUserRouteResponse){
            UUID id = UUID.fromString(userId);
        if(service.updateUserRoute(id,updateUserRouteResponse) &&  userSubscriptionService.studentPurchaseSubscription(userId)){
            return ResponseEntity.ok(true);

        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }


    @GetMapping("/student/checkJourney/{userId}/{date}")
    public ResponseEntity<Integer> checkJourney(@PathVariable UUID userId,@PathVariable LocalDate date){
//        UUID userId = UUID.fromString(id);
        try{
            int status = userJourneyService.markAttendanceStudents(userId,date);
            return ResponseEntity.ok(status);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/adult/daySelection/{userId}")
    public ResponseEntity<RouteDaysSelectionResponse> checkDaysThatSelected(@PathVariable UUID userId){
//        UUID userId = UUID.fromString(id);
        try{
            RouteDaysSelectionResponse routeDaysSelectionResponse = userJourneyService.getSelectedDaysByAdult(userId);
            return ResponseEntity.ok(routeDaysSelectionResponse);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }
}
