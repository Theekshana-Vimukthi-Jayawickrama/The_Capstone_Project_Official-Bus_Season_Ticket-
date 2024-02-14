package com.bus_season_ticket.capstone_project.adminUser;

import com.bus_season_ticket.capstone_project.User.*;
import com.bus_season_ticket.capstone_project.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.*;
import java.util.stream.Collectors;


@Service
@CrossOrigin
@RequiredArgsConstructor
public class AdminService {
    @Autowired
    private final DistrictListRepository districtListRepository;
    private final UserRepo userRepo;
    private final JavaMailSender emailSender;
    private final DeleteUserRepository deleteUserRepository;
    private final StudentBirthFilesRepo studentBirthFilesRepo;
    private final DeleteDistrictRepository deleteDistrictRepository;


    public boolean saveDistrict(DistrictRequest districtSch) {

        String district = districtSch.getDistrictName().toLowerCase().trim();
        Optional<DistrictList> schoolDistrict = districtListRepository.findByDistrictName(district);
        if (schoolDistrict.isPresent()) {
            return false;
        } else {

            DistrictList districtList = DistrictList.builder()
                    .districtName(district)
                    .adminId(districtSch.getAdminId())
                    .build();
            districtListRepository.save(districtList);
            return true;
        }
    }

    public List<String> getAllSchoolDistrict() {

        List<DistrictList> schoolDistrict = districtListRepository.findAll();
        if (schoolDistrict.isEmpty()) {
            return null;
        } else {
            List<String> districts = new ArrayList<>();

            for (int i = 0; i < schoolDistrict.size(); i++) {
                districts.add(schoolDistrict.get(i).getDistrictName().toUpperCase());
            }
            return districts;
        }
    }

    public List<GetUserResponse> getPendingUsers() {

        List<User> users = userRepo.findAll();
        if (users.isEmpty()) {
            return null;
        } else {
            List<GetUserResponse> userName = new ArrayList<GetUserResponse>();

            for (User user : users) {
                List<String> userRoles = user.getRoles().stream()
                        .map(role -> role.getRole().toString())
                        .collect(Collectors.toList());
                if(userRoles.contains(Role.STUDENT.toString().toUpperCase().trim()) || userRoles.contains(Role.ADULT.toString().trim().toUpperCase())){
                    if (Objects.equals(user.getUserStatusMaintain().getStatus(), "pending".toLowerCase().trim())) {
                        GetUserResponse pendingUser = GetUserResponse.builder()
                                .userName(user.getPersonalDetails().getFullName())
                                .id(user.getId())
                                .role(userRoles)
                                .status(user.getUserStatusMaintain().getStatus())
                                .build();
                        userName.add(pendingUser);
                    }
                }

            }
            return userName;
        }
    }
    public List<GetUserResponse> getActiveUsers() {

        List<User> users = userRepo.findAll();
        if (users.isEmpty()) {
            return null;
        } else {
            List<GetUserResponse> userName = new ArrayList<GetUserResponse>();

            for (User user : users) {
                List<String> userRoles = user.getRoles().stream()
                        .map(role -> role.getRole().toString())
                        .collect(Collectors.toList());
                if(userRoles.contains(Role.STUDENT.toString().toUpperCase().trim()) || userRoles.contains(Role.ADULT.toString().trim().toUpperCase())){
                    if (Objects.equals(user.getUserStatusMaintain().getStatus(), "active".toLowerCase().trim())) {
                        GetUserResponse pendingUser = GetUserResponse.builder()
                                .userName(user.getPersonalDetails().getFullName())
                                .id(user.getId())
                                .role(userRoles)
                                .status(user.getUserStatusMaintain().getStatus())
                                .build();
                        userName.add(pendingUser);
                    }
                }

            }
            return userName;
        }
    }

    public boolean userStatusUpdate(UUID userId, UserUpdateRequest userUpdateRequest, UUID adminId) {

        Optional<User> optionalUser = userRepo.findById(userId);
        Optional<User> adminUser = userRepo.findById(adminId);
        String userStatus;

        if(userUpdateRequest.isStatus()){
            userStatus = "active".toLowerCase().trim();
        }else{
            userStatus = "delete".toLowerCase().trim();
        }

        if (optionalUser.isPresent() && optionalUser.get().getUserStatusMaintain().getStatus().equals("pending".toLowerCase().trim())) {
            User user = optionalUser.get();
            user.getUserStatusMaintain().setStatus(userStatus);
            user.getUserStatusMaintain().setActivatedByAdminID(adminId);
            // Send approval email logic can go here

            if(userStatus.toLowerCase().trim().equals("active".toLowerCase().trim())){
                SimpleMailMessage message = new SimpleMailMessage();
                String toEmail = user.getEmail();
                message.setTo(toEmail);
                message.setSubject("Congratulations" + user.getPersonalDetails().getFullName() + "!");
                message.setText("You're eligible for our service. You can now log into your user account and make a payment to get the season ticket.");
                emailSender.send(message);
                userRepo.save(user);
                return true;
            }else if(userStatus.toLowerCase().trim().equals("delete".toLowerCase().trim())){
                if(adminUser.isPresent()){
                        DeleteUser deleteUser = DeleteUser.builder()
                                .userId(user.getId())
                                .userName(user.getUsername())
                                .fullName(user.getPersonalDetails().getFullName())
                                .reason(userUpdateRequest.getReason())
                                .adminId(adminId)
                                .build();
                        deleteUserRepository.save(deleteUser);
                        userRepo.deleteById(userId);

                    SimpleMailMessage message = new SimpleMailMessage();
                    String toEmail = user.getEmail();
                    message.setTo(toEmail);
                    message.setSubject( user.getPersonalDetails().getFullName() + ",I regret to share that, based on the credentials provided.");
                    message.setText("\n" +
                            "Apologies, it seems that the credentials provided aren't meeting the eligibility criteria for our service. Therefore, we're unable to accept them for accessing the user account or making the payment for the season ticket. Please try again with different credentials. It's possible that the ones provided don't meet the requirements for accessing our service.");
                    emailSender.send(message);
                    userRepo.deleteById(userId);
                    return true;
                    }else{
                        return false;
                    }
                }

            }
        return false;
    }




    public boolean removeUser(UUID userId, UUID adminId, UserDeleteRequest userDeleteRequest) {
        Optional<User> user = userRepo.findById(userId);
        Optional<User> adminUser = userRepo.findById(adminId);
        if(adminUser.isPresent()){
            if(user.isPresent()){
                DeleteUser deleteUser = DeleteUser.builder()
                        .userId(user.get().getId())
                        .userName(user.get().getUsername())
                        .fullName(user.get().getPersonalDetails().getFullName())
                        .reason(userDeleteRequest.getReason())
                        .adminId(adminId)
                        .build();
                deleteUserRepository.save(deleteUser);
                userRepo.deleteById(userId);
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    public PendingStudentDetailsResponse getStudentAllDetails(UUID userId) {
        Optional<User> user = userRepo.findById(userId);
        if (user.isPresent()) {
            List<String> userRoles = user.get().getRoles().stream()
                        .map(role -> role.getRole().toString())
                        .toList();
                if(userRoles.contains(Role.STUDENT.toString().toUpperCase().trim()) ){
                    if (Objects.equals(user.get().getUserStatusMaintain().getStatus(), "pending".toLowerCase().trim())) {
                        return PendingStudentDetailsResponse.builder()
                                .dob(user.get().getPersonalDetails().getDob())
                                .address(user.get().getPersonalDetails().getAddress())
                                .gender(user.get().getPersonalDetails().getGender())
                                .intName(user.get().getPersonalDetails().getIntName())
                                .residence(user.get().getPersonalDetails().getResidence())
                                .telephoneNumber(user.get().getPersonalDetails().getTelephoneNumber())
                                .fullName(user.get().getPersonalDetails().getFullName())
                                .contactNumber(user.get().getGuardianDetails().getContactNumber())
                                .guardianType(user.get().getGuardianDetails().getGuardianType())
                                .nameOfGuardian(user.get().getGuardianDetails().getNameOfGuardian())
                                .occupation(user.get().getGuardianDetails().getOccupation())
                                .route(user.get().getUserBusDetails().getRoute())
                                .charge(user.get().getUserBusDetails().getCharge())
                                .distance(user.get().getUserBusDetails().getDistance())
                                .nearestDeport(user.get().getUserBusDetails().getNearestDeport())
                                .indexNumber(user.get().getSchoolDetails().getIndexNumber())
                                .schAddress(user.get().getSchoolDetails().getSchAddress())
                                .district(user.get().getSchoolDetails().getDistrict())
                                .status(user.get().getUserStatusMaintain().getStatus())
                                .roles(userRoles)
                                .pendingRole(Role.STUDENT.toString())
                                .build();

                    }
                };
        }
        return null;
    }
    public PendingAdultDetailsResponse getAdultAllDetails(UUID userId) {
        Optional<User> user = userRepo.findById(userId);
        if (user.isPresent()) {

            List<String> userRoles = user.get().getRoles().stream()
                    .map(role -> role.getRole().toString())
                    .toList();
            System.out.println(userRoles);
          if ( userRoles.contains(Role.ADULT.toString().toUpperCase().trim())) {

                    return PendingAdultDetailsResponse.builder()
                            .fullName(user.get().getPersonalDetails().getFullName() != null ? user.get().getPersonalDetails().getFullName() : "null")
                            .intName(user.get().getPersonalDetails().getIntName() != null ? user.get().getPersonalDetails().getIntName() : "null")
                            .dob(user.get().getPersonalDetails().getDob() != null ? user.get().getPersonalDetails().getDob() : "null")
                            .address(user.get().getPersonalDetails().getAddress() != null ? user.get().getPersonalDetails().getAddress() : "null")
                            .gender(user.get().getPersonalDetails().getGender() != null ? user.get().getPersonalDetails().getGender() : "null")
                            .telephoneNumber(user.get().getPersonalDetails().getTelephoneNumber() != null  ? user.get().getPersonalDetails().getTelephoneNumber() : "null")
                            .residence(user.get().getPersonalDetails().getResidence() != null? user.get().getPersonalDetails().getResidence() : "null")
                            .route(user.get().getUserBusDetails().getRoute() != null ? user.get().getUserBusDetails().getRoute() : "null")
                            .distance(user.get().getUserBusDetails().getDistance() != null ? user.get().getUserBusDetails().getDistance() : "null")
                            .charge(user.get().getUserBusDetails().getCharge())
                            .nearestDeport(user.get().getUserBusDetails().getNearestDeport() != null && !user.get().getUserBusDetails().getNearestDeport().isEmpty() ? user.get().getUserBusDetails().getNearestDeport() : "null")
                            .monday(user.get().getSelectDays().isMonday())
                            .tuesday(user.get().getSelectDays().isTuesday())
                            .wednesday(user.get().getSelectDays().isWednesday())
                            .thursday(user.get().getSelectDays().isThursday())
                            .friday(user.get().getSelectDays().isFriday())
                            .saturday(user.get().getSelectDays().isSaturday())
                            .sunday(user.get().getSelectDays().isSunday())
                            .roles(userRoles)
                            .pendingRole(Role.ADULT.toString())
                            .status(user.get().getUserStatusMaintain().getStatus() != null ? user.get().getUserStatusMaintain().getStatus() : null)
                            .build();

          }
        }
        return null;
    }
    public byte[] downloadStudentBirthFiles(UUID userId) {
        Optional<User> user = userRepo.findById(userId);
        StudentBirthFiles dbImageData = null;
        if (user.isPresent()) {
            dbImageData = user.get().getStudentBirthFiles();
        }
        assert dbImageData != null;
        return ImageUtils.decompressImage(dbImageData.getFileBirthData());
    }
    public byte[] downloadApprovalLetter(UUID userId) {
        Optional<User> user = userRepo.findById(userId);
        ApprovalLetter dbImageData = null;
        if (user.isPresent()) {
            dbImageData = user.get().getApprovalLetter();
        }
        assert dbImageData != null;
        return ImageUtils.decompressImage(dbImageData.getData());
    }
    public byte[] downloadUserPhoto(UUID userId) {
        Optional<User> user = userRepo.findById(userId);
        UserPhotos dbImageData = null;
        if (user.isPresent()) {
            dbImageData = user.get().getUserPhoto();
        }
        assert dbImageData != null;
        return ImageUtils.decompressImage(dbImageData.getData());
    }

    public boolean updateDistrict(DistrictRequest districtName) {
        String district = districtName.getDistrictName().toLowerCase().trim();
        Optional<DistrictList> schoolDistrict = districtListRepository.findByDistrictName(district);
        if (schoolDistrict.isPresent()) {
            schoolDistrict.get().setAdminId(districtName.getAdminId());
            schoolDistrict.get().setDistrictName(districtName.getDistrictName());
            districtListRepository.save(schoolDistrict.get());
            return true;
        } else {
            return false;

        }
    }

    public boolean deleteDistrict(DistrictRequest districtName) {
        String district = districtName.getDistrictName().toLowerCase().trim();
        Optional<DistrictList> schoolDistrict = districtListRepository.findByDistrictName(district);
        if (schoolDistrict.isPresent()) {
           DeleteDistrictName deleteDistrictName = DeleteDistrictName.builder()
                   .adminId(districtName.getAdminId())
                   .districtName(districtName.getDistrictName())
                   .build();
            deleteDistrictRepository.save(deleteDistrictName);
            districtListRepository.delete(schoolDistrict.get());
            return true;
        } else {
            return false;
        }
    }
}
