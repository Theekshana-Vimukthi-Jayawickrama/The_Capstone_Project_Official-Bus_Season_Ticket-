package com.bus_season_ticket.capstone_project.auth;

import com.bus_season_ticket.capstone_project.User.*;
import com.bus_season_ticket.capstone_project.demo.EmailAlreadyExistsException;
import com.bus_season_ticket.capstone_project.demo.UserService;
import com.bus_season_ticket.capstone_project.busRoutes.BusRoute;
import com.bus_season_ticket.capstone_project.busRoutes.BusRouteRepository;
import com.bus_season_ticket.capstone_project.config.JwtService;
import com.bus_season_ticket.capstone_project.util.ImageUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@CrossOrigin
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    private final UserRepo repository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final StudentBirthFilesRepo uploadRepository;
    private final UserService userService;
    @Autowired
    private final StudentBirthFilesRepo studentBirthFilesRepo;
    @Autowired
    private final BusRouteRepository busRouteRepository;

    @Autowired
    private final ApprovalLetterRepository approvalLetterRepository;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request, SchoolRequest stuRequest, GuardianRequest guardianRequest, RouteRequest routeRequest, MultipartFile birthFile, MultipartFile approvalLetter, MultipartFile userPhoto) throws Exception {

        String email = request.getEmail();
        // Check if email already exists
        if (!userService.isEmailUnique(email)) {
            // Handle duplicate email error.
            throw new EmailAlreadyExistsException("Email already exists");
        }

        SchoolDetails schoolDetails = SchoolDetails.builder()
                .schAddress(stuRequest.getSchAddress())
                .district(stuRequest.getDistrict())
                .indexNumber(stuRequest.getIndexNumber())
                .build();

        GuardianDetails guardianDetails = GuardianDetails.builder()
                .nameOfGuardian(guardianRequest.getNameOfGuardian())
                .contactNumber(guardianRequest.getContactNumber())
                .occupation(guardianRequest.getOccupation())
                .guardianType(guardianRequest.getGuardianType())
                .build();

        String route = routeRequest.getRoute();
        String nearestDeport =routeRequest.getNearestDeport();
        Double charge = routeRequest.getCharge();
        UserBusDetails stuBusDetails = stuBusRoute(route,charge,nearestDeport);

       var user = User.builder()
               .fullName(request.getFullname())
                .intName(request.getIntName())
                .email(email)
                .userBusDetails(stuBusDetails)
                .schoolDetails(schoolDetails)
                .guardianDetails(guardianDetails)
                .gender(request.getGender())
                .dob(request.getDob())
                .telephoneNumber(request.getTelephone())
                .residence(request.getResidence())
                .verified(false)
                .address(request.getAddress())
                .status("Pending".trim().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
                .build();

        //Approval file save
        ApprovalLetter stuApprovalLetter = stuApprovalLetter(approvalLetter);
        if(stuApprovalLetter==null){
            throw new Exception("File could not be saved");
        }else{
            user.setApprovalLetter(stuApprovalLetter);
        }
        //Student Birth File
        StudentBirthFiles studentBirthFiles = stuBirthFile(birthFile);
        if(studentBirthFiles==null){
            throw new Exception("File could not be saved");
        }else{
            user.setStudentBirthFiles(studentBirthFiles);
        }

        //photo
        UserPhotos userPhoto1 = userPhotoUpload(userPhoto);
        if(userPhoto1==null){
            throw new Exception("File could not be saved");
        }else{
            user.setUserPhoto(userPhoto1);
        }

        repository.save(user);
        var jwtToken = JwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    //Student PDF letter
    @Transactional
    public ApprovalLetter stuApprovalLetter(MultipartFile file){

        try {
            ApprovalLetter pdfDocument = new ApprovalLetter();
            pdfDocument.setName(file.getOriginalFilename());
            pdfDocument.setType(file.getContentType());
            pdfDocument.setData(file.getBytes());
            return pdfDocument;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //Student BirthFile
    @Transactional
    public StudentBirthFiles stuBirthFile(MultipartFile file){

        try {
            StudentBirthFiles pdfDocument = new StudentBirthFiles();
            pdfDocument.setFileBirthName(file.getOriginalFilename());
            pdfDocument.setFileBirthType(file.getContentType());
            pdfDocument.setFileBirthData(file.getBytes());
            return pdfDocument;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
@Transactional
    public UserPhotos userPhotoUpload (MultipartFile file){

        try {
            UserPhotos photo = new UserPhotos();
            photo.setUserPhotoName(file.getOriginalFilename());
            photo.setPhotoType(file.getContentType());
            photo.setData(file.getBytes());
            return photo;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] downloadImage(Long fileId){
        Optional<StudentBirthFiles> dbImageData = uploadRepository.findById(fileId);
        return ImageUtils.decompressImage(dbImageData.get().getFileBirthData());
    }

    //BusRoute that student will go...
    public UserBusDetails stuBusRoute(String route, Double charge, String nearestDeport){
        BusRoute busRoute = busRouteRepository.findByRoute(route);
        try {
            if (busRoute != null) {
                String distance = busRoute.getDistance();

                return UserBusDetails.builder()
                        .charge(charge)
                        .distance(distance)
                        .route(route)
                        .nearestDeport(nearestDeport)
                        .build();
            } else {
                // Handle case where busRoute is not found for the given route
                return null;
            }
        } catch (Exception e) {
            // Handle any exceptions that might occur during processing
            return null;
        }
    }

    //Login..
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();

        if(Objects.equals(user.getStatus(), "active") && (user.getRole().equals(Role.ADULT) || user.getRole().equals(Role.STUDENT))){
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            var jwtToken = JwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .userId(user.getId())
                    .role(user.getRole().toString())
                    .build();
        }else{
            return null;
        }
    }

    public boolean isEmailUnique(String email) {
        return repository.findByEmail(email).isEmpty();
    }

    public boolean checkAlreadyUsers(String email) {
        Optional<User> user = repository.findByEmail(email);
        if(user.isPresent()){
            return true;
        }else{
            return false;
        }
    }

    public String getName(UUID userId) {
        Optional<User> user = repository.findById(userId);

        if(user.isPresent()){
            return  user.get().getIntName();
        }else{
            return "No name";
        }

    }
    public boolean changePassword(String userEmail, RequestPassword requestPassword) {
        Optional<User> user = repository.findByEmail(userEmail);
        if(user.isPresent()&& Objects.equals(user.get().getStatus(), "active".trim().toLowerCase())){
            user.ifPresent(userUpdate ->{
                userUpdate.setPassword( passwordEncoder.encode(requestPassword.getPassword()));
                repository.save(userUpdate);
            });
            return true;
        }else {
            return false;
        }
    }
}
