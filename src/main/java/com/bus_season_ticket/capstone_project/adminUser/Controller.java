package com.bus_season_ticket.capstone_project.adminUser;


import com.bus_season_ticket.capstone_project.Deport.AddDepotRequest;
import com.bus_season_ticket.capstone_project.Deport.DepotService;
import com.bus_season_ticket.capstone_project.Deport.DeleteDeportRequest;
import com.bus_season_ticket.capstone_project.QRcode.QRCodeService;
import com.bus_season_ticket.capstone_project.User.*;
import com.bus_season_ticket.capstone_project.auth.AuthenticationResponse;
import com.bus_season_ticket.capstone_project.auth.AuthenticationService;
import com.bus_season_ticket.capstone_project.auth.RegisterRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/admin")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class Controller {

    @Autowired
    private final AdminService adminService;
    @Autowired
    private final QRCodeService qrCodeService;
    private final AuthenticationService service;
    @Autowired
    private final UsersService userService;
    private  final ObjectMapper mapperAdult;
    @Autowired
    private final UserRepo userRepo;
    @Autowired
    private final ApprovalLetterRepository pdfDocumentRepository;
    @Autowired
    private final StudentBirthFilesRepo studentBirthFilesRepo;
    @Autowired
    private final DepotService depotService;

    @PostMapping("/add/depot/{adminId}")
    public ResponseEntity<?> depotAdd(@RequestBody AddDepotRequest addDepotRequest, @PathVariable UUID adminId) {
        try {
            if(depotService.addDeport(addDepotRequest,adminId)){
                return ResponseEntity.status(HttpStatus.OK).build();
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
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

    @PutMapping("/updateSchoolDistrict/{id}")
    public ResponseEntity<?> updateSchoolDistrict(@RequestBody DistrictRequest districtName, @PathVariable int id) {

        boolean status = adminService.updateDistrict(districtName, id);
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

    @DeleteMapping("/deleteSchoolDistrict/{id}")
    public ResponseEntity<?> deleteSchoolDistrict(@RequestBody DistrictRequestDelete districtName,@PathVariable int id) {

        boolean status = adminService.deleteDistrict(districtName,id);
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
    @GetMapping("/getAllSchDistrict")
    public ResponseEntity<List<GetDistrictsResponse>> getAllSchoolDistricts (){

        try {
            List<GetDistrictsResponse> districts = adminService.getAllSchoolDistrict();

            if(districts == null){
                return ResponseEntity.notFound().build();
            }else{
                return ResponseEntity.ok(districts);
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getAllRemoveSchDistrict")
    public ResponseEntity<List<DistrictRequestDelete>> getAllRemoveSchoolDistricts (){

        try {
            List<DistrictRequestDelete> districts = adminService.getAllDeleteSchoolDistrict();

            if(districts == null){
                return ResponseEntity.notFound().build();
            }else{
                return ResponseEntity.ok(districts);
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getConductorAllDetails/{userId}")
    public ResponseEntity<GetAllConductorDetailsResponse> getConductorAllDetails(@PathVariable UUID userId){

        try {
            GetAllConductorDetailsResponse user = adminService.getConductorAllDetails(userId);


            return ResponseEntity.ok(user);

        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getAdminAllDetails/{userId}")
    public ResponseEntity<GetAllAdminDetailsResponse> getAdminAllDetails(@PathVariable UUID userId){

        try {
            GetAllAdminDetailsResponse user = adminService.getAdminAllDetails(userId);


            return ResponseEntity.ok(user);

        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getConductorShortDetails")
    public ResponseEntity<List<GetShortConductorDetailsResponse>> getConductorShortDetails(){
        try {
            return ResponseEntity.ok(adminService.getConductorShortDetails());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/deport/{id}")
    public ResponseEntity<?> updateRoute(@RequestBody AddDepotRequest addDepotRequest, @PathVariable int id) {
        try {
            boolean status = depotService.updateDeport(addDepotRequest,id);
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

    @DeleteMapping("/deleteDeport/{id}")
    public ResponseEntity<?> deleteDeport(@RequestBody DeleteDeportRequest deport, @PathVariable int id) {

        boolean status = depotService.deleteDeport(deport,id);
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


    @GetMapping("singleDeport/getDetails/{id}")
    public ResponseEntity<AddDepotRequest> getDeportShortDetails(@PathVariable int id){
        try {

            return ResponseEntity.ok(depotService.getSingleDeportOfDeport(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getAdminShortDetails")
    public ResponseEntity<List<GetShortAdminDetailsResponse>> getAdminShortDetails(){
        try {
            return ResponseEntity.ok(adminService.getAdminShortDetails());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getDeleteUsersDetails")
    public ResponseEntity<List<DeleteUserDetailsResponse>> getDeleteDetails(){
        try {
            return ResponseEntity.ok(adminService.DeleteUSerDetails());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getRoles")
    public ResponseEntity<List<String>> getRoles(){
        try {
            return ResponseEntity.ok(adminService.getRoles());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updateConductorDetails/{userId}")
    public ResponseEntity<Boolean> updateConductorDetails(@PathVariable UUID userId,
                                                          @RequestPart("request") String getAllConductorDetailsResponse,
                                                          @RequestParam(value = "photo", required = false ) MultipartFile userPhoto){
        try {
            GetAllConductorDetailsResponse getAllConductorDetailsResponse1 = mapperAdult.readValue(getAllConductorDetailsResponse,GetAllConductorDetailsResponse.class);
            return ResponseEntity.ok(adminService.UpdateConductorDetails(userId, userPhoto, getAllConductorDetailsResponse1));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getAllDeleteDepots")
    public ResponseEntity<List<DeleteDeportRequest>> getAllDeleteDepots (){

        try {
            List<DeleteDeportRequest> depots = depotService.getAllDeleteDepotName();

            if(depots == null){
                return ResponseEntity.notFound().build();
            }else{
                return ResponseEntity.ok(depots);
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/updateAdminDetails/{userId}")
    public ResponseEntity<Boolean> updateAdminDetails(@PathVariable UUID userId,
                                                          @RequestPart("request") String getAllConductorDetailsResponse,
                                                          @RequestParam(value = "photo", required = false ) MultipartFile userPhoto){
        try {
            GetAllAdminDetailsResponse getAllConductorDetailsResponse1 = mapperAdult.readValue(getAllConductorDetailsResponse,GetAllAdminDetailsResponse.class);
            return ResponseEntity.ok(adminService.UpdateAdminDetails(userId, userPhoto, getAllConductorDetailsResponse1));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/conductor/register" )
    public ResponseEntity<AuthenticationResponse> conductorRegister(
            @RequestPart("request") String request1,
            @RequestParam("photo") MultipartFile userPhoto,
            HttpServletRequest req
    ) throws JsonProcessingException {

        try {
            RegisterRequest userRequest = mapperAdult.readValue(request1, RegisterRequest.class);
            Optional<User> user = userRepo.findByEmail(userRequest.getEmail());
            if(user.isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }else{
                AuthenticationResponse response = service.conductorRegister(userRequest,userPhoto);
                return ResponseEntity.ok(response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/register" )
    public ResponseEntity<AuthenticationResponse> aminRegister(
            @RequestPart("request") String request1,
            @RequestParam("photo") MultipartFile userPhoto,
            HttpServletRequest req
    ) throws JsonProcessingException {

        try {
            RegisterRequest userRequest = mapperAdult.readValue(request1, RegisterRequest.class);
            Optional<User> user = userRepo.findByEmail(userRequest.getEmail());
            if(user.isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }else{
                AuthenticationResponse response = service.adminRegister(userRequest,userPhoto);
                return ResponseEntity.ok(response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/removeUser/{userId}/{adminId}")
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



//    @PostMapping("/addUsers/{adminId}")
//    public ResponseEntity<String>addRoles(@RequestBody AddRolesRequest addRolesRequest ,@PathVariable UUID adminId ){
//        List<UserRoles> userRoles = roleRepository.findAll();
//        if(userRoles.contains(addRolesRequest.getRole().trim().toUpperCase())){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already role exist");
//        }else{
//            Role role = Role.valueOf(addRolesRequest.getRole().toUpperCase().trim());
//            UserRoles userRoles1 = UserRoles.builder()
//                    .role(role)
//                    .adminId(adminId)
//                    .build();
//            roleRepository.save(userRoles1);
//            return ResponseEntity.ok().body("added");
//        }
//    }




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

    @GetMapping("/approvalLetter/{id}")
    public ResponseEntity<byte[]> downloadPDF(@PathVariable UUID id) {
        Optional<User> user = userRepo.findById(id);

        if(user.isPresent()){
            int itemId = user.get().getApprovalLetter().getId();
            ApprovalLetter pdfDocument = pdfDocumentRepository.findById(itemId)
                    .orElse(null);
            if (pdfDocument != null) {
                return ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=" + pdfDocument.getName())
                        .body(pdfDocument.getData());
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/studentBirthPDF/{id}")
    public ResponseEntity<byte[]> birthPDF(@PathVariable UUID id) {
        Optional<User> user = userRepo.findById(id);

        if(user.isPresent()){
            long itemId = user.get().getStudentBirthFiles().getId();
            StudentBirthFiles pdfDocument = studentBirthFilesRepo.findById(itemId)
                    .orElse(null);
            if (pdfDocument != null) {
                return ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=" + pdfDocument.getFileBirthName())
                        .body(pdfDocument.getFileBirthData());
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

}
