package com.finalproject.airport.maintenance.service;

import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaimLocation;
import com.finalproject.airport.airplane.baggageclaim.repository.BaggageClaimRepository;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounterLocation;
import com.finalproject.airport.airplane.checkincounter.repository.CheckinCounterRepository;
import com.finalproject.airport.airplane.gate.repository.GateRepository;
import com.finalproject.airport.airplane.gate.service.GateService;
import com.finalproject.airport.approval.entity.ApprovalEntity;
import com.finalproject.airport.facilities.repository.FacilitiesRepository;
import com.finalproject.airport.maintenance.dto.MaintenanceDTO;
import com.finalproject.airport.maintenance.entity.MaintenanceEntity;
import com.finalproject.airport.maintenance.repository.MaintenanceRepository;
import com.finalproject.airport.storage.repository.StorageRepository;
import com.finalproject.airport.store.repository.StoreRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final ModelMapper modelMapper;
    private final GateService gateService;
    private final GateRepository gateRepository;
    private final BaggageClaimRepository baggageClaimRepository;
    private final FacilitiesRepository facilitiesRepository;
    private final CheckinCounterRepository checkinCounterRepository;
    private final StorageRepository storageRepository;
    private final StoreRepository storeRepository;

    @Autowired
    public MaintenanceService(MaintenanceRepository maintenanceRepository , ModelMapper modelMapper, GateService gateService, GateRepository gateRepository, BaggageClaimRepository baggageClaimRepository, FacilitiesRepository facilitiesRepository, CheckinCounterRepository checkinCounterRepository, StorageRepository storageRepository, StoreRepository storeRepository) {
        this.maintenanceRepository = maintenanceRepository;
        this.modelMapper = modelMapper;
        this.gateService = gateService;
        this.gateRepository = gateRepository;
        this.baggageClaimRepository = baggageClaimRepository;
        this.facilitiesRepository = facilitiesRepository;
        this.checkinCounterRepository = checkinCounterRepository;
        this.storageRepository = storageRepository;
        this.storeRepository = storeRepository;
    }

    // 정비 전체 조회
    public List<MaintenanceDTO> findAll() {
        List<MaintenanceEntity> maintenanceList = maintenanceRepository.findByIsActive("Y");
        System.out.println("maintenanceList = " + maintenanceList);
        return maintenanceList.stream()
                .map(maintenance -> modelMapper.map(maintenance, MaintenanceDTO.class))
                .collect(Collectors.toList());
    }

    // 정비 상세 조회
    public MaintenanceDTO getMaintenanceById(int maintenanceCode) {

        MaintenanceEntity maintenanceEntity = maintenanceRepository.findById(maintenanceCode);
        return modelMapper.map(maintenanceEntity, MaintenanceDTO.class);
    }

    // 정비 항목 수정
    @Transactional
    public void updateMaintenance(int maintenanceCode, MaintenanceDTO maintenanceDTO) {

        MaintenanceEntity maintenanceEntity = maintenanceRepository.findBymaintenanceCode(maintenanceCode);

        maintenanceEntity = maintenanceEntity.toBuilder()
                .maintenanceStructure(maintenanceDTO.getStructure())
                .type(maintenanceDTO.getType())
                .location(maintenanceDTO.getLocation())
                .status(maintenanceDTO.getStatus())
                .manager(
                        maintenanceDTO.getManager())
                .maintenanceEquipment(maintenanceDTO.getEquipment())
                .maintenanceNumber(maintenanceDTO.getNumber())
                .maintenanceExpense(maintenanceDTO.getExpense())
                .maintenanceStartDate(maintenanceDTO.getMaintenanceStartDate())
                .maintenanceEndDate(maintenanceDTO.getMaintenanceEndDate())
                .maintenanceDetails(maintenanceDTO.getMaintenanceDetails())
                .build();

        maintenanceRepository.save(maintenanceEntity);
    }

    // 정비 삭제
    @Transactional
    public void softDelete(int maintenanceCode) {

        MaintenanceEntity maintenanceEntity = maintenanceRepository.findBymaintenanceCode(maintenanceCode);

        maintenanceEntity = maintenanceEntity.toBuilder().isActive("N").build();

        maintenanceRepository.save(maintenanceEntity);
    }

    @Transactional
    public String insertMaintenance(MaintenanceDTO maintenanceDTO) {

        MaintenanceEntity maintenanceEntity = modelMapper.map(maintenanceDTO, MaintenanceEntity.class);
        MaintenanceEntity maintenanceEntitySaved = maintenanceRepository.save(maintenanceEntity);

        if (maintenanceDTO.getStructure().equals("gate")) {
            Integer getCode = gateRepository.findbylocation(maintenanceDTO.getLocation());

            maintenanceEntitySaved = maintenanceEntitySaved.toBuilder().gate(getCode).build();


        } else if (maintenanceDTO.getStructure().equals("baggageClaim")) {

            BaggageClaimLocation location = BaggageClaimLocation.valueOf(maintenanceDTO.getLocation());
            Integer getCode = baggageClaimRepository.findbylocation(location);

            maintenanceEntitySaved = maintenanceEntitySaved.toBuilder().baggageClaim(getCode).build();

        } else if (maintenanceDTO.getStructure().equals("checkinCounter")) {

            CheckinCounterLocation location = CheckinCounterLocation.valueOf(maintenanceDTO.getLocation());

            Integer getCode = checkinCounterRepository.findbylocation(location);
            maintenanceEntitySaved = maintenanceEntitySaved.toBuilder().checkinCounter(getCode).build();

        }else if (maintenanceDTO.getStructure().equals("facilities")) {
            Integer getCode = facilitiesRepository.findbylocation(maintenanceDTO.getLocation());

            maintenanceEntitySaved = maintenanceEntitySaved.toBuilder().facilities(getCode).build();

        }
//        else if(maintenanceDTO.getStructure().equals("store")){
//            Integer getCode = storeRepository.findbylocation(maintenanceDTO.getLocation());
//
//            maintenanceEntitySaved = maintenanceEntitySaved.toBuilder().store(getCode).build();
//        }
        else if(maintenanceDTO.getStructure().equals("storage")){
            Integer getCode = storageRepository.findbylocation(maintenanceDTO.getLocation());

            maintenanceEntitySaved = maintenanceEntitySaved.toBuilder().store(getCode).build();
        }



        return "정비 등록 성공";
        }


    public List<Object> findlocation(String structure) {
        System.out.println("structure = " + structure);
        List<Object> locations = new ArrayList<>();

        if(structure.equals("gate")){
            List<Integer> locationList = gateRepository.findAlllocations();
            System.out.println("locationList = " + locationList);

            for(Integer gate : locationList){
                locations.add(gate);
            }
            System.out.println("locations = " + locations);
        } else if (structure.equals("baggageClaim")) {
            List<BaggageClaimLocation> locationList = Arrays.asList(BaggageClaimLocation.values());

            for(BaggageClaimLocation location : locationList){
                locations.add(location);
            }
            System.out.println("locations = " + locations);
        } else if (structure.equals("checkinCounter")) {
            List<CheckinCounterLocation> locationList = Arrays.asList(CheckinCounterLocation.values());

            for(CheckinCounterLocation location : locationList){
                locations.add(location);
            }
            System.out.println("locations = " + locations);
        }else if (structure.equals("facilities")) {
            List<String> locationList = facilitiesRepository.findAlllocations();
            System.out.println("locationList = " + locationList);

            for(String facilities : locationList){
                locations.add(facilities);
            }
            System.out.println("locations = " + locations);
        }


        return locations;
    }
}
