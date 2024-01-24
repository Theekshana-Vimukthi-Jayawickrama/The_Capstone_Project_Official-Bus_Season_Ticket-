package com.bus_season_ticket.capstone_project.User;

import com.bus_season_ticket.capstone_project.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final AuthenticationService service;
    private final UsersService userService;

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
}
