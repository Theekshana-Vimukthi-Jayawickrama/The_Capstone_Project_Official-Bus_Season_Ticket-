package com.bus_season_ticket.capstone_project.User;

import com.bus_season_ticket.capstone_project.busRoutes.BusRoute;
import com.bus_season_ticket.capstone_project.busRoutes.BusRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;
import java.util.UUID;

@Service
@CrossOrigin
@RequiredArgsConstructor
public class UsersService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    BusRouteRepository busRouteRepository;

    public Optional<UserPhotos> getProfilePhoto(String userId) {
        Optional<User> user = userRepo.findById(UUID.fromString(userId));

        UserPhotos userPhotos = user.get().getUserPhoto();;
        return Optional.ofNullable(userPhotos);
    }

    public Optional<RouteResponse> getRouteDetails(String userId) {
        Optional<User> user = userRepo.findById(UUID.fromString(userId));
        BusRoute busRoute = busRouteRepository.findByRoute(user.get().getUserBusDetails().getRoute());
        if(user.isPresent()){
            RouteResponse routeResponse = RouteResponse.builder()
                    .routeNo(busRoute.getRouteNo())
                    .charge(user.get().getUserBusDetails().getCharge())
                    .distance(busRoute.getDistance())
                    .route(user.get().getUserBusDetails().getRoute())
                    .build();
            return Optional.ofNullable(routeResponse);
        }else{
            return null;
        }
    }
}
