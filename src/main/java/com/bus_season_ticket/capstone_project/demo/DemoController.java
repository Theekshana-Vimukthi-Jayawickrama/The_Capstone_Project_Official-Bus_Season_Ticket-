package com.bus_season_ticket.capstone_project.demo;


import com.bus_season_ticket.capstone_project.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/demo-controller")
@RequiredArgsConstructor
public class DemoController {
    private final AuthenticationService service;

    @GetMapping("/download/{fileId}")
    public ResponseEntity<?> downloadImage(@PathVariable Long fileId){
        byte[] imageData=service.downloadImage(fileId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }

}
