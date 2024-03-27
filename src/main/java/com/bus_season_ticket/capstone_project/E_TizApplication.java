
package com.bus_season_ticket.capstone_project;

import com.bus_season_ticket.capstone_project.User.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@CrossOrigin
@SpringBootApplication
@EnableScheduling
public class E_TizApplication implements CommandLineRunner {

	@Autowired
	private final RoleRepository roleRepository;
	private final UserRepo userRepo;
	private final PasswordEncoder passwordEncoder;

	public E_TizApplication(RoleRepository roleRepository, UserRepo userRepo, PasswordEncoder passwordEncoder) {
		this.roleRepository = roleRepository;
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}

	public static void main(String[] args) {
		SpringApplication.run(E_TizApplication .class, args);
	}

	public void run(String... args) throws IOException {
		Optional<UserRoles> adminAccount = roleRepository.findByRole(Role.ADMIN);

		if(adminAccount.isEmpty()){

			Set<UserRoles> roles = new HashSet<>();
			roles.add(UserRoles.builder()
					.role(Role.ADMIN)
					.build());


			byte[] imageData = getImageBytes();

			// Create a UserPhotos object and save the image to the database
			UserPhotos photo = UserPhotos.builder()
					.userPhotoName("example.jpg")
					.PhotoType("image/png")
					.data(imageData)
					.build();

			PersonalDetails personalDetails = PersonalDetails.builder()
					.dob("0000/00/00")
					.telephoneNumber("00000000")
					.residence("NULL")
					.gender("NULL")
					.fullName("Default Admin")
					.intName("NULL")
					.address("NULL")
					.build();
			SuperAdminStatusMaintain superAdminMaintain = SuperAdminStatusMaintain.builder()
					.status(true)
					.build();

			User user = new User();
			user.setRoles(roles);
			user.setSuperAdminMaintain(superAdminMaintain);
			user.setEmail("admin@gmail.com");
			user.setUserName("admin@gmail.com");
			user.setUserPhoto(photo);
			user.setPassword(passwordEncoder.encode("admin1234"));
			user.setPersonalDetails(personalDetails);

			userRepo.save(user);
		}
		List<UserRoles> allRoles = roleRepository.findAll();
		List<Role> role = Arrays.stream(Role.values()).toList();
		for (Role userRole : role) {
			if(allRoles.stream().allMatch(userRoles -> userRoles.getRole() != userRole )){
					UserRoles userRoles = UserRoles.builder()
							.role(userRole)
							.build();
					roleRepository.save(userRoles);
			}
		}
	}

	private byte[] getImageBytes() throws IOException {
		// Load the image file from the package
		ClassPathResource resource = new ClassPathResource( "/logo.png");
		Path imagePath = Paths.get(resource.getURI());

		// Read the image bytes
		return Files.readAllBytes(imagePath);
	}

}

