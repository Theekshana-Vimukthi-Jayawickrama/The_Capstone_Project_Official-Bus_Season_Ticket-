package com.bus_season_ticket.capstone_project.Deport;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class DepotService {

    @Autowired
    private final  DepotRepository depotRepository;
    private final DeleteDeportRepo deleteDeportRepo;
    public boolean addDeport(AddDepotRequest addDepotRequest, UUID adminId) {
        List<DepotDetails> depotDetailsList = depotRepository.findAll();

        if(depotDetailsList.isEmpty()){
            DepotDetails depotDetails1 = DepotDetails.builder()
                    .depotName(addDepotRequest.getDepotName().toUpperCase().trim())
                    .phoneNumber(addDepotRequest.getPhoneNumber())
                    .phoneNumber2(addDepotRequest.getPhoneNumber2())
                    .adminId(adminId)
                    .build();
            depotRepository.save(depotDetails1);
            return true;
        }else{
            for(DepotDetails depotDetails: depotDetailsList){
                if(depotDetails.getDepotName().toUpperCase().trim().equals(addDepotRequest.getDepotName().toUpperCase().trim())){
                    return false;
                }else{
                    DepotDetails depotDetails1 = DepotDetails.builder()
                            .depotName(addDepotRequest.getDepotName().toUpperCase().trim())
                            .phoneNumber(addDepotRequest.getPhoneNumber())
                            .phoneNumber2(addDepotRequest.getPhoneNumber2())
                            .adminId(adminId)
                            .build();
                    depotRepository.save(depotDetails1);
                    return true;
                }
            }
        }
    return false;
    }

    public List<AddDepotRequest> getAllDepotName() {

        List< DepotDetails> depot = depotRepository.findAll();
        if (depot.isEmpty()) {
            return null;
        } else {
            List<AddDepotRequest> depots = new ArrayList<>();

            for (int i = 0; i < depot.size(); i++) {
                AddDepotRequest getDepotResponse = AddDepotRequest.builder()
                        .depotName(depot.get(i).getDepotName())
                        .phoneNumber(depot.get(i).getPhoneNumber())
                        .phoneNumber2(depot.get(i).getPhoneNumber2())
                        .id(depot.get(i).getId())
                        .build();
                depots.add(getDepotResponse);
            }
            return depots;
        }
    }

    public List<DeleteDeportRequest> getAllDeleteDepotName() {

        List< DeleteDeport> depot = deleteDeportRepo.findAll();
        if (depot.isEmpty()) {
            return null;
        } else {
            List<DeleteDeportRequest> depots = new ArrayList<>();

            for (int i = 0; i < depot.size(); i++) {
                DeleteDeportRequest getDepotResponse = DeleteDeportRequest.builder()
                        .name(depot.get(i).getDepotName())
                        .adminId(depot.get(i).getAdminId())
                        .reason(depot.get(i).getReason())
                        .date(depot.get(i).getDate())
                        .build();
                depots.add(getDepotResponse);
            }
            return depots;
        }
    }

    public boolean updateDeport(AddDepotRequest addDepotRequest, int id) {
        Optional<DepotDetails> deport= depotRepository.findById(id);

            if(deport.isPresent()){
                    deport.get().setDepotName(addDepotRequest.getDepotName().toUpperCase().trim());
                    deport.get().setPhoneNumber(addDepotRequest.getPhoneNumber());
                    deport.get().setPhoneNumber2(addDepotRequest.getPhoneNumber2());
                    depotRepository.save(deport.get());
                return true;
            }else{
                return false;
            }

    }

    public AddDepotRequest getSingleDeportOfDeport(int id) {
        Optional<DepotDetails> depotDetails =depotRepository.findById(id);

        if(depotDetails.isPresent()){
            return AddDepotRequest.builder()
                    .depotName(depotDetails.get().getDepotName())
                    .phoneNumber(depotDetails.get().getPhoneNumber())
                    .phoneNumber2(depotDetails.get().getPhoneNumber2())
                    .build();
        }else{
            return null;
        }
    }

    public boolean deleteDeport(DeleteDeportRequest deport, int id) {
        Optional<DepotDetails> depotDetails =depotRepository.findById(id);

        if(depotDetails.isPresent()){
          DeleteDeport deleteDeport = DeleteDeport.builder()
                  .reason(deport.getReason())
                  .adminId(deport.getAdminId())
                  .depotName(depotDetails.get().getDepotName())
                  .date(LocalDate.now())
                  .build();
          deleteDeportRepo.save(deleteDeport);
          depotRepository.delete(depotDetails.get());
          return true;

        }else{
            return false;
        }
    }
}
