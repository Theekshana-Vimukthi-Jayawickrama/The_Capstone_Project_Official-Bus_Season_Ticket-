package com.bus_season_ticket.capstone_project.auth;


import com.bus_season_ticket.capstone_project.User.ApprovalLetter;
import com.bus_season_ticket.capstone_project.User.ApprovalLetterRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final ObjectMapper mapper;
    private  final  ObjectMapper mapperAdult;
    @Autowired
    private ApprovalLetterRepository pdfDocumentRepository;


    @GetMapping("/{id}")
    public ResponseEntity<byte[]> downloadPDF(@PathVariable Integer id) {
        ApprovalLetter pdfDocument = pdfDocumentRepository.findById(id)
                .orElse(null);
        if (pdfDocument != null) {
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + pdfDocument.getName())
                    .body(pdfDocument.getData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/register" )
    public ResponseEntity<AuthenticationResponse> register(
            @RequestPart("schoolDetailRequest") String schoolRequest1,
            @RequestPart("guardianDetailsRequest") String guardianRequest1,
            @RequestPart("routeDetails") String routeRequest1,
            @RequestPart("request") String request1,
            @RequestParam("birthFile") MultipartFile birthFile,
            @RequestParam("approvalFile") MultipartFile approvalFile,
            @RequestParam("photo") MultipartFile userPhoto,
            HttpServletRequest req
    ) throws JsonProcessingException {
        System.out.println("Received Headers:");
        req.getHeaderNames().asIterator()
                .forEachRemaining(headerName ->
                        System.out.println(headerName + ": " + req.getHeader(headerName))
                );
        SchoolRequest schoolRequest = mapper.readValue(schoolRequest1, SchoolRequest.class);
        GuardianRequest guardianRequest = mapper.readValue(guardianRequest1,GuardianRequest.class);
        RouteRequest routeRequest = mapper.readValue(routeRequest1, RouteRequest.class);
        RegisterRequest request = mapper.readValue(request1, RegisterRequest.class);
        try {

            AuthenticationResponse response = service.register(request,schoolRequest,guardianRequest,routeRequest,birthFile,approvalFile,userPhoto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/adult/register" )
    public ResponseEntity<AuthenticationResponse> adultRegister(
            @RequestPart("routeDetails") String routeRequest1,
            @RequestPart("request") String request1,
            @RequestParam("uploadedNICFront") MultipartFile uploadedNICFront,
            @RequestParam("uploadedNICBack") MultipartFile uploadedNICBack,
            @RequestParam("photo") MultipartFile userPhoto,
            @RequestPart("selectDays") String selectionDays,
            HttpServletRequest req
    ) throws JsonProcessingException {
        System.out.println("Received Headers:");
        req.getHeaderNames().asIterator()
                .forEachRemaining(headerName ->
                        System.out.println(headerName + ": " + req.getHeader(headerName))
                );
        RouteRequest routeRequest = mapperAdult.readValue(routeRequest1, RouteRequest.class);
        RouteDaysSelectionRequest selectionDays1 = mapperAdult.readValue(selectionDays, RouteDaysSelectionRequest.class);
        RegisterRequest request = mapperAdult.readValue(request1, RegisterRequest.class);

        try {

            AuthenticationResponse response = service.adultRegister(request,selectionDays1,routeRequest,userPhoto, uploadedNICFront,uploadedNICBack);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/conductor/register" )
    public ResponseEntity<AuthenticationResponse> conductorRegister(
            @RequestBody RegisterRequest request,
            HttpServletRequest req
    ) throws JsonProcessingException {


        try {

            AuthenticationResponse response = service.conductorRegister(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/sendOTP")
    public ResponseEntity<String> sendOTP(
            @RequestParam("email") String email
    ) {
        try {
            service.sendOTP(email);
            return ResponseEntity.ok("OTP sent successful.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("User with provided email doesn't exist");
        }

    }

    @PostMapping("/reSendOTP")
    public ResponseEntity<String> reSendOTP(
            @RequestParam("email") String email
    ) {
        try {
            String message = service.reSendOTP(email);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("User with provided email doesn't exist.");
        }

    }

    @PostMapping("/verifyOTP")
    public ResponseEntity<String> verifyOTP(
            @RequestParam("email") String email,
            @RequestParam("otp") Integer otp
    ) {
        try {
            String status = service.verifyOTP(email, otp);
            if (Objects.equals(status, "OTP verification successful.")) {
                return ResponseEntity.ok(status);
            } else if (Objects.equals(status, "OTP is expired")) {
                return ResponseEntity.badRequest().body("OTP is expired");
            } else if (Objects.equals(status, "Invalid OTP")) {
                return ResponseEntity.badRequest().body("Invalid OTP");
            } else if (Objects.equals(status, "Email does not exit")) {
                return ResponseEntity.badRequest().body("Email does not exit");
            }else{
                return ResponseEntity.badRequest().body("verification unsuccessful");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/checkAlreadyUsers/{email}")
    public ResponseEntity<String> checkAlreadyUsers(
            @PathVariable String email){
        boolean status =service.checkAlreadyUsers(email);
        try{
            if(status){
                return  ResponseEntity.ok("User has already registered");
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid request");
        }

    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ){
                AuthenticationResponse token = service.authenticate(request);
                if(token != null){
                    return ResponseEntity.ok(token);
                }else{
                    return ResponseEntity.badRequest().body(null);
                }
    }

    @PostMapping("/conductor/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateConductor(
            @RequestBody AuthenticationRequest request
    ){
        AuthenticationResponse token = service.authenticateConductor(request);
        if(token != null){
            return ResponseEntity.ok(token);
        }else{
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/updatePassword/{userEmail}")
    public ResponseEntity<Boolean> changePassword(@PathVariable String userEmail,@RequestBody RequestPassword requestPassword){
        boolean status = service.changePassword(userEmail,requestPassword);

        if(status){
            return ResponseEntity.ok(true);
        }else{
            return ResponseEntity.ok(false);
        }
    }
}

