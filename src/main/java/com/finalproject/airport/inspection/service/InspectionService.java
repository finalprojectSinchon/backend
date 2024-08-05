package com.finalproject.airport.inspection.service;

import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaim;
import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaimLocation;
import com.finalproject.airport.airplane.baggageclaim.repository.BaggageClaimRepository;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounter;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounterLocation;
import com.finalproject.airport.airplane.checkincounter.repository.CheckinCounterRepository;
import com.finalproject.airport.airplane.gate.entity.Gate;
import com.finalproject.airport.airplane.gate.repository.GateRepository;
import com.finalproject.airport.airplane.gate.service.GateService;
import com.finalproject.airport.equipment.repository.EquipmentRepository;
import com.finalproject.airport.facilities.entity.FacilitiesEntity;
import com.finalproject.airport.facilities.repository.FacilitiesRepository;
import com.finalproject.airport.inspection.dto.InspectionDTO;
import com.finalproject.airport.inspection.entity.InspectionEntity;
import com.finalproject.airport.inspection.respository.InspectionRepository;
import com.finalproject.airport.maintenance.repository.MaintenanceEquipmentRepository;
import com.finalproject.airport.maintenance.repository.MaintenanceRepository;
import com.finalproject.airport.storage.entity.StorageEntity;
import com.finalproject.airport.storage.repository.StorageRepository;
import com.finalproject.airport.store.entity.StoreEntity;
import com.finalproject.airport.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
//@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectionService {

    private final InspectionRepository inspectionRepository;


    private final ModelMapper modelMapper;
    private final GateRepository gateRepository;
    private final BaggageClaimRepository baggageClaimRepository;
    private final FacilitiesRepository facilitiesRepository;
    private final CheckinCounterRepository checkinCounterRepository;
    private final StorageRepository storageRepository;
    private final StoreRepository storeRepository;
    private final EquipmentRepository equipmentRepository;
    private final MaintenanceEquipmentRepository maintenanceEquipmentRepository;

    @Autowired
    public InspectionService(InspectionRepository inspectionRepository , ModelMapper modelMapper, GateRepository gateRepository, BaggageClaimRepository baggageClaimRepository, FacilitiesRepository facilitiesRepository, CheckinCounterRepository checkinCounterRepository, StorageRepository storageRepository, StoreRepository storeRepository, EquipmentRepository equipmentRepository, MaintenanceEquipmentRepository maintenanceEquipmentRepository) {
        this.inspectionRepository = inspectionRepository;
        this.modelMapper = modelMapper;
        this.gateRepository = gateRepository;
        this.baggageClaimRepository = baggageClaimRepository;
        this.facilitiesRepository = facilitiesRepository;
        this.checkinCounterRepository = checkinCounterRepository;
        this.storageRepository = storageRepository;
        this.storeRepository = storeRepository;
        this.equipmentRepository = equipmentRepository;
        this.maintenanceEquipmentRepository = maintenanceEquipmentRepository;
    }


    //점검 전체 조회
    public List<InspectionDTO> getInspectionList() {

        List<InspectionDTO> inspectionDTOList = new ArrayList<>();
        List<InspectionEntity> inspectionList1 = inspectionRepository.findByIsActive("Y");
        inspectionList1.forEach(inspectionEntity -> {inspectionDTOList.add(modelMapper.map(inspectionEntity, InspectionDTO.class));});


        return inspectionDTOList;
    }

    //점검 상세 조회
    public InspectionDTO getInspection(int inspectionCode) {
        InspectionEntity inspection = inspectionRepository.findByinspectionCode(inspectionCode);

        return modelMapper.map(inspection, InspectionDTO.class);
    }


    // 점검 등록
    @Transactional
    public String addInspection(InspectionDTO inspectionDTO) {
        System.out.println("이ㅏㄴ스퓨ㅔㄱ션 나옴 ? = " + inspectionDTO);
        InspectionEntity inspectionEntity = modelMapper.map(inspectionDTO, InspectionEntity.class);
        InspectionEntity inspectionEntitySaved = inspectionRepository.save(inspectionEntity);


        if (inspectionDTO.getStructure().equals("gate")) {
            Gate gate = gateRepository.findByLocation(Integer.valueOf(inspectionDTO.getLocation()));


            System.out.println("gate = " + gate);
            inspectionEntitySaved = inspectionEntitySaved.toBuilder().gate(gate).build();


            System.out.println("inspectionEntitySaved = " + inspectionEntitySaved);

        } else if (inspectionDTO.getStructure().equals("baggageClaim")) {

            BaggageClaimLocation location = BaggageClaimLocation.valueOf(inspectionDTO.getLocation());
            BaggageClaim baggageClaim = baggageClaimRepository.findByLocation(location);

            System.out.println("baggageClaim = " + baggageClaim);
            inspectionEntitySaved = inspectionEntitySaved.toBuilder().baggageClaim(baggageClaim).build();
            System.out.println("maintenanceEntitySaved = " + inspectionEntitySaved);

        } else if (inspectionDTO.getStructure().equals("checkinCounter")) {

            CheckinCounterLocation location = CheckinCounterLocation.valueOf(inspectionDTO.getLocation());
            System.out.println("CheckinCounterLocation ? = " + location);
            CheckinCounter checkinCounter = checkinCounterRepository.findByLocation(location);
            System.out.println("checkinCounter = " + checkinCounter);

            inspectionEntitySaved = inspectionEntitySaved.toBuilder().checkinCounter(checkinCounter).build();

        }else if (inspectionDTO.getStructure().equals("facilities")) {
            FacilitiesEntity facilities = facilitiesRepository.findByLocation(inspectionDTO.getLocation());

            inspectionEntitySaved = inspectionEntitySaved.toBuilder().facilities(facilities).build();

        }
        else if(inspectionDTO.getStructure().equals("store")){
            StoreEntity store = storeRepository.findBystoreLocation(inspectionDTO.getLocation());

            inspectionEntitySaved = inspectionEntitySaved.toBuilder().store(store).build();
        }
        else if(inspectionDTO.getStructure().equals("storage")){
            StorageEntity storage = storageRepository.findByLocation(inspectionDTO.getLocation());

            inspectionEntitySaved = inspectionEntitySaved.toBuilder().storage(storage).build();
        }

        inspectionRepository.save(inspectionEntitySaved);

        return "정비 등록 성공";



    }

    // 점검 수정
    public void updateInspection(int inspectionCode, InspectionDTO inspectionDTO) {

        InspectionEntity inspectionEntity = inspectionRepository.findByinspectionCode(inspectionCode);


        System.out.println("inspectionEntity = " + inspectionEntity);
        inspectionEntity = modelMapper.map(inspectionDTO, InspectionEntity.class);
        inspectionRepository.save(inspectionEntity);

    }

    // 점검 삭제
    public void softDeleteInspection(int inspectionCode) {
        InspectionEntity inspectionEntity = inspectionRepository.findById(Integer.valueOf(inspectionCode)).orElseThrow(IllegalArgumentException::new);
        inspectionEntity = inspectionEntity.toBuilder().isActive("N").build();


        inspectionRepository.save(inspectionEntity);

    }

    @Transactional
    public String insertInspection(InspectionDTO inspectionDTO) {

        int result = 0;
        try {
            InspectionEntity insertInspection =modelMapper.map(inspectionDTO, InspectionEntity.class);
            inspectionRepository.save(insertInspection);

            result = 1;

        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return(result > 0)? "정비 등록 성공": "정비 등록 실패";
    }
}
