package com.bus_season_ticket.capstone_project.auth;


import com.bus_season_ticket.capstone_project.JourneyMaker.SelectDays;
import com.bus_season_ticket.capstone_project.OTPGenerator.OTP;
import com.bus_season_ticket.capstone_project.OTPGenerator.OTPRepository;
import com.bus_season_ticket.capstone_project.User.*;
import com.bus_season_ticket.capstone_project.busRoutes.BusRoute;
import com.bus_season_ticket.capstone_project.busRoutes.BusRouteRepository;
import com.bus_season_ticket.capstone_project.config.JwtService;
import com.bus_season_ticket.capstone_project.demo.EmailAlreadyExistsException;
import com.bus_season_ticket.capstone_project.demo.UserService;
import com.bus_season_ticket.capstone_project.util.ImageUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private final OTPRepository otpRepository;
    private final JavaMailSender emailSender;
    @Autowired
    private final BusRouteRepository busRouteRepository;
    @Autowired
    private final ApprovalLetterRepository approvalLetterRepository;
    private final RoleRepository roleRepository;

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

        PersonalDetails personalDetails = PersonalDetails.builder()
                .dob(request.getDob())
                .telephoneNumber(request.getTelephone())
                .residence(request.getResidence())
                .gender(request.getGender())
                .fullName(request.getFullname())
                .intName(request.getIntName())
                .address(request.getAddress())
                .build();

        UserStatusMaintain userStatusMaintain = UserStatusMaintain.builder()
                .verified(false)
                .status("Pending".trim().toLowerCase())
                .build();

        Set<UserRoles> roles = new HashSet<>();
        roles.add(
                roleRepository.findByRole(Role.STUDENT).orElseThrow(() -> new IllegalArgumentException("Role not Found"))
    );

       var user = User.builder()
                .email(email)
               .personalDetails(personalDetails)
               .userStatusMaintain(userStatusMaintain)
                .userBusDetails(stuBusDetails)
                .schoolDetails(schoolDetails)
                .guardianDetails(guardianDetails)
                .userName(email.toLowerCase().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
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

    public AuthenticationResponse adultRegister(RegisterRequest request,RouteDaysSelectionRequest daysSelectionRequest,  RouteRequest routeRequest, MultipartFile userPhoto, MultipartFile NICFrontPhoto,MultipartFile NICBackPhoto) throws Exception {
        String email = request.getEmail();
        // Check if email already exists
        if (!userService.isEmailUnique(email)) {
            // Handle duplicate email error.
            throw new EmailAlreadyExistsException("Email already exists");
        }
        SelectDays selectDays = SelectDays.builder()
                .friday(daysSelectionRequest.isFriday())
                .monday(daysSelectionRequest.isMonday())
                .sunday(daysSelectionRequest.isSunday())
                .saturday(daysSelectionRequest.isSaturday())
                .tuesday(daysSelectionRequest.isTuesday())
                .thursday(daysSelectionRequest.isThursday())
                .wednesday(daysSelectionRequest.isWednesday())
                .build();

        String route = routeRequest.getRoute();
        String nearestDeport =routeRequest.getNearestDeport();
        Double charge = routeRequest.getCharge();
        UserBusDetails stuBusDetails = stuBusRoute(route,charge,nearestDeport);

        PersonalDetails personalDetails = PersonalDetails.builder()
                .dob(request.getDob())
                .telephoneNumber(request.getTelephone())
                .residence(request.getResidence())
                .gender(request.getGender())
                .fullName(request.getFullname())
                .intName(request.getIntName())
                .address(request.getAddress())
                .build();

        UserStatusMaintain userStatusMaintain = UserStatusMaintain.builder()
                .verified(false)
                .status("Pending".trim().toLowerCase())
                .build();

        Set<UserRoles> roles = new HashSet<>();
        roles.add(
                roleRepository.findByRole(Role.ADULT).orElseThrow(() -> new IllegalArgumentException("Role not Found"))
        );

        var user = User.builder()
                .email(email)
                .personalDetails(personalDetails)
                .userStatusMaintain(userStatusMaintain)
                .userBusDetails(stuBusDetails)
                .selectDays(selectDays)
                .userName(email.toLowerCase().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .build();

        NICAdultBack userBackNIC = userNICBackPhoto(NICBackPhoto);
        if(userBackNIC==null){
            throw new Exception("File could not be saved");
        }else{
            user.setAdultBackNIC(userBackNIC);
        }
        NICAdultFront userFrontNIC = userNICFrontPhoto(NICFrontPhoto);
        if(userFrontNIC==null){
            throw new Exception("File could not be saved");
        }else{
            user.setAdultFrontNIC(userFrontNIC);
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

    public NICAdultBack userNICBackPhoto (MultipartFile file){

        try {
            NICAdultBack photo = new NICAdultBack();
            photo.setPhotoNICBackName(file.getOriginalFilename());
            photo.setNICType(file.getContentType());
            photo.setData(file.getBytes());
            return photo;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public NICAdultFront userNICFrontPhoto (MultipartFile file){

        try {
            NICAdultFront photo = new NICAdultFront();
            photo.setUserPhotoName(file.getOriginalFilename());
            photo.setPhotoType(file.getContentType());
            photo.setData(file.getBytes());
            return photo;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Email works and OTP generate...
        public boolean sendOTPEmail(String toEmail, Integer otp) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(toEmail);
                message.setSubject("OTP Verification");
                message.setText("Your OTP for verification is: " + otp + ". This will expire within TWO minutes.");
                emailSender.send(message);
                return true; // Email sent successfully
            } catch (MailException e) {
                // Handle exceptions (e.g., log or perform appropriate actions)
                e.printStackTrace();
                return false; // Email sending failed
            }
        }
    public void sendOTP(String email)  {
        Optional<OTP> userOTP = otpRepository.findByEmail(email);

        if(userOTP.isPresent()){
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiryTime = now.plusMinutes(2);
            Integer newOTP = generateOTP();
            OTP otp = userOTP.get();
            otp.setOtpCode(newOTP);
            otp.setOtpExpiryTime(expiryTime);
            otpRepository.save(otp);
            sendOTPEmail(email, newOTP);
        }else{
            // Generate a new OTP
            Integer OTP = generateOTP();
            // Save the new OTP for the user
            saveOTP(email, OTP);
            // Send the new OTP to the user's email
            sendOTPEmail(email, OTP);
        }

    }
    @Transactional
    public String reSendOTP (String email){
        Integer newOTP = generateOTP();

        Optional<OTP> userOTP = otpRepository.findByEmail(email);
        if(userOTP.isPresent()){
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiryTime = now.plusMinutes(2);
            OTP otp = userOTP.get();
            otp.setOtpCode(newOTP);
            otp.setOtpExpiryTime(expiryTime);
            otpRepository.save(otp);
            sendOTPEmail(email, newOTP);
            return "new OTP sent.";
        }else{
            return null;
        }
    }
    @Transactional
    public void saveOTP(String email, Integer otpCode) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryTime = now.plusMinutes(2); // Set OTP expiry to 2 minutes from now

        OTP otp = OTP.builder()
                .otpCode(otpCode)
                .email(email)
                .otpExpiryTime(expiryTime)
                .build();

        otpRepository.save(otp); // Save OTP in the database

    }

    // Generate OTP method
    public Integer generateOTP() {
        User user = new User();
        String numbers = "0123456789";
        Random random = new SecureRandom();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            otp.append(numbers.charAt(random.nextInt(numbers.length())));
        }
        return Integer.valueOf(otp.toString());
    }

    public String verifyOTP(String email, Integer otp) {
        Optional<OTP> userOTP= otpRepository.findByEmail(email);

        if(userOTP.isPresent()){

            if (userOTP.get().getOtpCode() != null && userOTP.get().getOtpCode().equals(otp)) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime expiryTime = userOTP.get().getOtpExpiryTime();

                // Check if OTP has expired
                if (now.isBefore(expiryTime)) {
                    // OTP is valid and not expired
                    return "OTP verification successful.";
                } else {
                    // OTP is expired
                    return "OTP is expired";
                }
            }else{return "Invalid OTP";}
        }
        else{return "Email does not exit.";}
    }


}
