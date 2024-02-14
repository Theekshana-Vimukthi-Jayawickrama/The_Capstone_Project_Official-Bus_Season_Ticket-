package com.bus_season_ticket.capstone_project.adminUser;


import com.bus_season_ticket.capstone_project.QRcode.QRCodeService;
import com.bus_season_ticket.capstone_project.User.*;
import com.bus_season_ticket.capstone_project.demo.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@CrossOrigin
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class Controller {

    @Autowired
    private final AdminService adminService;
    @Autowired
    private final QRCodeService qrCodeService;
    @Autowired
    private final UsersService userService;
    @Autowired
    private final UserRepo userRepo;


    @GetMapping("/getAllPendingUsers")
    public ResponseEntity<List<GetUserResponse>> getPendingUsers() {
        try {
                return ResponseEntity.ok(adminService.getPendingUsers());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/getAllActiveUsers")
    public ResponseEntity<List<GetUserResponse>> getActiveUsers() {
        try {
            return ResponseEntity.ok(adminService.getActiveUsers());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/setUserStatus/generateQRCode/{userId}/{adminId}")
    public ResponseEntity<?> approveUser(@PathVariable UUID userId,@PathVariable UUID adminId,@RequestBody UserUpdateRequest userUpdateRequest) {
        try {
                if(adminService.userStatusUpdate(userId,userUpdateRequest,adminId)){
                    if(qrCodeService.saveQRCode(userId)){
                        return ResponseEntity.ok("QR Code generated and saved successfully!, & status updated");
                    }else{
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("status updated but Failed to generate QR Code: ");
                    }
                }else{
                    return ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body("User not found or already active user.");
                }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/addSchoolDistrict")
    public ResponseEntity<?> setSchoolDistrict(@RequestBody DistrictRequest districtName) {

        boolean status = adminService.saveDistrict(districtName);
        try {
            if (status) {
                return ResponseEntity.ok("Saved");
            } else {
                return ResponseEntity.badRequest().body("Already added");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/updateSchoolDistrict")
    public ResponseEntity<?> updateSchoolDistrict(@RequestBody DistrictRequest districtName) {

        boolean status = adminService.updateDistrict(districtName);
        try {
            if (status) {
                return ResponseEntity.ok("Updated");
            } else {
                return ResponseEntity.badRequest().body("Already updated");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/deleteSchoolDistrict")
    public ResponseEntity<?> deleteSchoolDistrict(@RequestBody DistrictRequest districtName) {

        boolean status = adminService.deleteDistrict(districtName);
        try {
            if (status) {
                return ResponseEntity.ok("Deleted");
            } else {
                return ResponseEntity.badRequest().body("Already deleted");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/removeUser/{userId}/{adminId}")
    public ResponseEntity<?> removeUsers(
            @PathVariable UUID userId,
            @PathVariable UUID adminId,
            @RequestBody UserDeleteRequest userDeleteRequest
            ) {

        try {
            if (adminService.removeUser(userId,adminId,userDeleteRequest)) {
                return ResponseEntity.ok("Successful");
            } else {
                return ResponseEntity.badRequest().body("Unsuccessful");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getAllSchDistrict")
    public ResponseEntity<List<String>> getAllSchoolDistricts (){

        try {
            List<String> districts = adminService.getAllSchoolDistrict();

            if(districts == null){
                return ResponseEntity.notFound().build();
            }else{
                return ResponseEntity.ok(districts);
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/getStudentAllDetails/{userId}")
    public ResponseEntity<PendingStudentDetailsResponse> getStudentAllDetails(@PathVariable UUID userId){

        try {
            PendingStudentDetailsResponse user = adminService.getStudentAllDetails(userId);

            if(user == null){
                return ResponseEntity.notFound().build();
            }else{
                return ResponseEntity.ok(user);
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/getAdultAllDetails/{userId}")
    public ResponseEntity<PendingAdultDetailsResponse> getAdultAllDetails(@PathVariable UUID userId){

        try {
            PendingAdultDetailsResponse user = adminService.getAdultAllDetails(userId);


                return ResponseEntity.ok(user);

        }catch (Exception e){
            return ResponseEntity.badRequest().build();
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
    @GetMapping("/NICFrontPhoto/{id}")
    public ResponseEntity<byte[]> downloadPhoto(@PathVariable UUID id) {
        Optional<User> user = userRepo.findById(id);
        if(user.isPresent()){
            NICAdultFront userPhotos = user.get().getAdultFrontNIC();

            byte[] imageData = userPhotos.getData();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Adjust content type based on your image format

            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/NICBackPhoto/{id}")
    public ResponseEntity<byte[]> downloadNICPhoto(@PathVariable UUID id) {
        Optional<User> user = userRepo.findById(id);
        if(user.isPresent()){
            NICAdultBack userPhotos = user.get().getAdultBackNIC();
                byte[] imageData = userPhotos.getData();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG); // Adjust content type based on your image format

                return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

    }



}
