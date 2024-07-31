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

    @Autowired
    public MaintenanceService(MaintenanceRepository maintenanceRepository , ModelMapper modelMapper, GateService gateService, GateRepository gateRepository, BaggageClaimRepository baggageClaimRepository, FacilitiesRepository facilitiesRepository, CheckinCounterRepository checkinCounterRepository) {
        this.maintenanceRepository = maintenanceRepository;
        this.modelMapper = modelMapper;
        this.gateService = gateService;
        this.gateRepository = gateRepository;
        this.baggageClaimRepository = baggageClaimRepository;
        this.facilitiesRepository = facilitiesRepository;
        this.checkinCounterRepository = checkinCounterRepository;
    }

    // 정비 전체 조회
    public List<MaintenanceDTO> findAll() {
        List<MaintenanceEntity> maintenanceList = maintenanceRepository.findByIsActive("Y");
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
                .maintenanceType(maintenanceDTO.getType())
                .maintenanceLocation(maintenanceDTO.getLocation())
                .maintenanceStatus(maintenanceDTO.getStatus())
                .maintenanceManager(maintenanceDTO.getManager())
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

//        if(maintenanceDTO.getStructure().equals("gate")){
//            Integer getCode = gateRepository.findbylocation(maintenanceDTO.getLocation());
//
//
//        } else if (maintenanceDTO.getStructure().equals("baggageClaim")) {
//            Integer getCode = baggageClaimRepository.findbylocation(maintenanceDTO.getLocation());
//
//        } else if (maintenanceDTO.getStructure().equals("checkinCounter")) {
//            Integer getCode = checkinCounterRepository.findbylocation(maintenanceDTO.getLocation());
//
//        }else if (maintenanceDTO.getStructure().equals("facilities")) {
//            Integer getCode = facilitiesRepository.findbylocation(maintenanceDTO.getLocation());
//
//        }else if(maintenanceDTO.getStructure().equals("store")){
//            Integer getCode = gateRepository.findbylocation(maintenanceDTO.getLocation());
//        }
//
//        MaintenanceEntity insertMaintenance = new MaintenanceEntity(
//                maintenanceDTO.getGateCode(),
//                maintenanceDTO.getCheckinCounterCode(),
//                maintenanceDTO.getBaggageClaimCode(),
//                maintenanceDTO.getStoreCode(),
//                maintenanceDTO.getStorageCode(),
//                maintenanceDTO.getFacilitiesCode()
//        );
//
//
//
//        maintenanceRepository.save(insertMaintenance);




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
