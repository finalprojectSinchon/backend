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
import com.finalproject.airport.maintenance.entity.MaintenanceEquipment;
import com.finalproject.airport.maintenance.repository.MaintenanceEquipmentRepository;
import com.finalproject.airport.maintenance.repository.MaintenanceRepository;
import com.finalproject.airport.storage.entity.StorageEntity;
import com.finalproject.airport.storage.repository.StorageRepository;
import com.finalproject.airport.store.entity.StoreEntity;
import com.finalproject.airport.store.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
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
    private final MaintenanceEquipmentRepository maintenanceEquipmentRepository;

    @Autowired
    public MaintenanceService(MaintenanceRepository maintenanceRepository, ModelMapper modelMapper, GateService gateService, GateRepository gateRepository, BaggageClaimRepository baggageClaimRepository, FacilitiesRepository facilitiesRepository, CheckinCounterRepository checkinCounterRepository, StorageRepository storageRepository, StoreRepository storeRepository, EquipmentRepository equipmentRepository, MaintenanceEquipmentRepository maintenanceEquipmentRepository) {
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
        this.maintenanceEquipmentRepository = maintenanceEquipmentRepository;
    }

    // 정비 전체 조회
    public List<MaintenanceDTO> findAll() {
        List<MaintenanceEntity> maintenanceList = maintenanceRepository.findByIsActive("Y");
        log.info("Maintenance List: {}", maintenanceList);
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
                .manager(maintenanceDTO.getManager())
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
            Gate gate = gateRepository.findByGateCode(Integer.valueOf(maintenanceDTO.getLocation())).orElseThrow();
            log.info("Gate: {}", gate);
            maintenanceEntitySaved = maintenanceEntitySaved.toBuilder().gate(gate).build();
            log.info("Updated Maintenance Entity: {}", maintenanceEntitySaved);

        } else if (maintenanceDTO.getStructure().equals("baggageClaim")) {
            BaggageClaimLocation location = BaggageClaimLocation.valueOf(maintenanceDTO.getLocation());
            BaggageClaim baggageClaim = baggageClaimRepository.findByLocation(location);
            log.info("Baggage Claim: {}", baggageClaim);
            maintenanceEntitySaved = maintenanceEntitySaved.toBuilder().baggageClaim(baggageClaim).build();
            log.info("Updated Maintenance Entity: {}", maintenanceEntitySaved);

        } else if (maintenanceDTO.getStructure().equals("checkinCounter")) {
            CheckinCounterLocation location = CheckinCounterLocation.valueOf(maintenanceDTO.getLocation());
            CheckinCounter checkinCounter = checkinCounterRepository.findByLocation(location);
            maintenanceEntitySaved = maintenanceEntitySaved.toBuilder().checkinCounter(checkinCounter).build();

        } else if (maintenanceDTO.getStructure().equals("facilities")) {
            FacilitiesEntity facilities = facilitiesRepository.findByLocation(maintenanceDTO.getLocation());
            maintenanceEntitySaved = maintenanceEntitySaved.toBuilder().facilities(facilities).build();

        } else if (maintenanceDTO.getStructure().equals("storage")) {
            StorageEntity storage = storageRepository.findByLocation(maintenanceDTO.getLocation());
            maintenanceEntitySaved = maintenanceEntitySaved.toBuilder().storage(storage).build();
        }else if (maintenanceDTO.getStructure().equals("store")) {
            StoreEntity store = storeRepository.findBystoreLocation(maintenanceDTO.getLocation());
            maintenanceEntitySaved = maintenanceEntitySaved.toBuilder().store(store).build();
        }

        maintenanceRepository.save(maintenanceEntitySaved);

        return "Maintenance registration successful";
    }

    public List<Object> findLocation(String structure) {
        log.info("Structure: {}", structure);
        List<Object> locations = new ArrayList<>();

        if (structure.equals("gate")) {
            List<Integer> locationList = gateRepository.findAlllocations();
            log.info("Gate Location List: {}", locationList);
            locations.addAll(locationList);

        } else if (structure.equals("baggageClaim")) {
            List<BaggageClaimLocation> locationList = Arrays.asList(BaggageClaimLocation.values());
            locations.addAll(locationList);

        } else if (structure.equals("checkinCounter")) {
            List<CheckinCounterLocation> locationList = Arrays.asList(CheckinCounterLocation.values());
            locations.addAll(locationList);

        } else if (structure.equals("facilities")) {
            List<String> locationList = facilitiesRepository.findAlllocations();
            log.info("Facilities Location List: {}", locationList);
            locations.addAll(locationList);
        } else if (structure.equals("store")) {
            List<String> locationList = storeRepository.findAlllocations();
            log.info("store Location List: {}", locationList);
            locations.addAll(locationList);
        } else if (structure.equals("storage")) {
            List<String> locationList = storageRepository.findAlllocations();
            log.info("storage Location List: {}", locationList);
            locations.addAll(locationList);
        }

        log.info("Locations: {}", locations);
        return locations;
    }

    @Transactional
    public void maintenanceEquipment(MaintenanceEquipmentDTO maintenanceEquipment) {

        List<EquipmentQuantityDTO> equipment = maintenanceEquipment.getEquipment();
        MaintenanceEntity maintenance = maintenanceRepository.findBymaintenanceCode(maintenanceEquipment.getMaintenance().getMaintenanceCode());

        for (EquipmentQuantityDTO equip : equipment) {
            EquipmentEntity equipmentEntity = equipmentRepository.findByequipmentCode(equip.getEquipment());
            int newQuantity = equipmentEntity.getEquipmentQuantity() - equip.getQuantity();
            equipmentEntity = equipmentEntity.toBuilder().equipmentQuantity(newQuantity).build();
            equipmentRepository.save(equipmentEntity);

            EquipmentEntity equipment1 = equipmentRepository.findByequipmentCode(equip.getEquipment());
            MaintenanceEquipment maintenanceEquip = new MaintenanceEquipment();
            maintenanceEquip = maintenanceEquip.toBuilder()
                    .maintenance(maintenance)
                    .equipment(equipment1)
                    .quantity(equip.getQuantity()).build();

            maintenanceEquipmentRepository.save(maintenanceEquip);
        }

        List<MaintenanceEquipment> maintenanceEquipments = maintenanceEquipmentRepository.findBymaintenance(maintenance);
        log.info("Maintenance Equipments: {}", maintenanceEquipments);
        int price = 0;
        for (MaintenanceEquipment maintenanceEquip : maintenanceEquipments) {
            int total = maintenanceEquip.getQuantity() * maintenanceEquip.getEquipment().getEquipmentPrice();
            price += total;

            MaintenanceEntity maintenance1 = maintenanceRepository.findBymaintenanceCode(maintenanceEquip.getMaintenance().getMaintenanceCode());
            maintenance1 = maintenance1.toBuilder().price(price).build();
            maintenanceRepository.save(maintenance1);
        }
    }

    public int getMaintenanceEquipment(int maintenanceCode) {
        int result = 0;
        MaintenanceEntity maintenance = maintenanceRepository.findBymaintenanceCode(maintenanceCode);
        List<MaintenanceEquipment> me = maintenanceEquipmentRepository.findBymaintenance(maintenance);

        if (me.size() > 0) {
            result = 1;
        }
        return result;
    }
}
