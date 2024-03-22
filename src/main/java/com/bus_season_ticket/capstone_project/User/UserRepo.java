package com.bus_season_ticket.capstone_project.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email); //find user by email. that is why use it.

    Optional<User> findById (UUID id);

    Optional<User> findByUserName(String UserName);


}
