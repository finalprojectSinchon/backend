package com.finalproject.airport.maintenance.service;

import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaim;
import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaimLocation;
import com.finalproject.airport.airplane.baggageclaim.repository.BaggageClaimRepository;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounter;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounterLocation;
import com.finalproject.airport.airplane.checkincounter.repository.CheckinCounterRepository;
import com.finalproject.airport.airplane.gate.entity.Gate;
import com.finalproject.airport.airplane.gate.repository.GateRepository;
import com.finalproject.airport.airplane.gate.service.GateService;
import com.finalproject.airport.approval.entity.ApprovalEntity;
import com.finalproject.airport.equipment.entity.EquipmentEntity;
import com.finalproject.airport.equipment.repository.EquipmentRepository;
import com.finalproject.airport.facilities.entity.FacilitiesEntity;
import com.finalproject.airport.facilities.repository.FacilitiesRepository;
import com.finalproject.airport.maintenance.dto.EquipmentQuantityDTO;
import com.finalproject.airport.maintenance.dto.MaintenanceDTO;
import com.finalproject.airport.maintenance.dto.MaintenanceEquipmentDTO;
import com.finalproject.airport.maintenance.entity.MaintenanceEntity;
import com.finalproject.airport.maintenance.repository.MaintenanceRepository;
import com.finalproject.airport.storage.entity.StorageEntity;
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
    private final EquipmentRepository equipmentRepository;

    @Autowired
    public MaintenanceService(MaintenanceRepository maintenanceRepository , ModelMapper modelMapper, GateService gateService, GateRepository gateRepository, BaggageClaimRepository baggageClaimRepository, FacilitiesRepository facilitiesRepository, CheckinCounterRepository checkinCounterRepository, StorageRepository storageRepository, StoreRepository storeRepository, EquipmentRepository equipmentRepository) {
        this.maintenanceRepository = maintenanceRepository;
        this.modelMapper = modelMapper;
        this.gateService = gateService;
        this.gateRepository = gateRepository;
        this.baggageClaimRepository = baggageClaimRepository;
        this.facilitiesRepository = facilitiesRepository;
        this.checkinCounterRepository = checkinCounterRepository;
        this.storageRepository = storageRepository;
        this.storeRepository = storeRepository;
        this.equipmentRepository = equipmentRepository;
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
            Gate gate = gateRepository.findByLocation(Integer.valueOf(maintenanceDTO.getLocation()));

            System.out.println("gate = " + gate);
            maintenanceEntitySaved = maintenanceEntitySaved.toBuilder().gate(gate).build();


            System.out.println("maintenanceEntitySaved = " + maintenanceEntitySaved);

        } else if (maintenanceDTO.getStructure().equals("baggageClaim")) {

            BaggageClaimLocation location = BaggageClaimLocation.valueOf(maintenanceDTO.getLocation());
            BaggageClaim baggageClaim = baggageClaimRepository.findByLocation(location);

            System.out.println("baggageClaim = " + baggageClaim);
            maintenanceEntitySaved = maintenanceEntitySaved.toBuilder().baggageClaim(baggageClaim).build();
            System.out.println("maintenanceEntitySaved = " + maintenanceEntitySaved);

        } else if (maintenanceDTO.getStructure().equals("checkinCounter")) {

            CheckinCounterLocation location = CheckinCounterLocation.valueOf(maintenanceDTO.getLocation());
            CheckinCounter checkinCounter = checkinCounterRepository.findByLocation(location);

            maintenanceEntitySaved = maintenanceEntitySaved.toBuilder().checkinCounter(checkinCounter).build();

        }else if (maintenanceDTO.getStructure().equals("facilities")) {
            FacilitiesEntity facilities = facilitiesRepository.findByLocation(maintenanceDTO.getLocation());

            maintenanceEntitySaved = maintenanceEntitySaved.toBuilder().facilities(facilities).build();

        }
//        else if(maintenanceDTO.getStructure().equals("store")){
//            Store store = storeRepository.findByLocation(maintenanceDTO.getLocation());
//
//            maintenanceEntitySaved = maintenanceEntitySaved.toBuilder().store(store).build();
//        }
        else if(maintenanceDTO.getStructure().equals("storage")){
            StorageEntity storage = storageRepository.findByLocation(maintenanceDTO.getLocation());

            maintenanceEntitySaved = maintenanceEntitySaved.toBuilder().storage(storage).build();
        }

        maintenanceRepository.save(maintenanceEntitySaved);

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

    @Transactional
    public void maintenanceEquipment(MaintenanceEquipmentDTO maintenanceEquipment) {


        List<EquipmentQuantityDTO> equipment = maintenanceEquipment.getEquipment();

        for(EquipmentQuantityDTO equip : equipment){
            EquipmentEntity equipmentEntity =  equipmentRepository.findByequipmentCode(equip.getEquipment());
                equipmentEntity = equipmentEntity.toBuilder()
                        .equipmentQuantity(equipmentEntity.getEquipmentQuantity() - equip.getQuantity())
                        .build();

                equipmentRepository.save(equipmentEntity);

        }




    }
}
