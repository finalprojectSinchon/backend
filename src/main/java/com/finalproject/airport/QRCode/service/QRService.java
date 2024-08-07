package com.finalproject.airport.QRCode.service;


import com.finalproject.airport.QRCode.dto.*;
import com.finalproject.airport.airplane.baggageclaim.dto.BaggageClaimDTO;
import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaim;
import com.finalproject.airport.airplane.baggageclaim.repository.BaggageClaimRepository;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounter;
import com.finalproject.airport.airplane.checkincounter.repository.CheckinCounterRepository;
import com.finalproject.airport.airplane.gate.entity.Gate;
import com.finalproject.airport.airplane.gate.repository.GateRepository;
import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.facilities.entity.FacilitiesEntity;
import com.finalproject.airport.facilities.repository.FacilitiesRepository;
import com.finalproject.airport.inspection.entity.InspectionEntity;
import com.finalproject.airport.inspection.respository.InspectionRepository;
import com.finalproject.airport.location.entity.LocationEntity;
import com.finalproject.airport.location.repository.LocationRepository;
import com.finalproject.airport.storage.entity.StorageEntity;
import com.finalproject.airport.storage.repository.StorageRepository;
import com.finalproject.airport.store.dto.StoreDTO;
import com.finalproject.airport.store.entity.StoreEntity;
import com.finalproject.airport.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QRService {

    private final ModelMapper modelMapper;

    private final BaggageClaimRepository baggageClaimRepository;

    private final StoreRepository storeRepository;

    private final GateRepository gateRepository;

    private final CheckinCounterRepository checkinCounterRepository;

    private final StorageRepository storageRepository;

    private final FacilitiesRepository facilitiesRepository;

    private final LocationRepository locationRepository;

    private final InspectionRepository inspectionRepository;

    public ResponseEntity<?> getQRList(String qrType) {

        QRAllDTO qrAllDTO = new QRAllDTO();

        switch (qrType) {
            case "baggage_claim" :
                List<BaggageClaim> baggageClaimList = baggageClaimRepository.findByisActive("Y");
                List<BaggageClaimQRDTO> baggageClaimDTOList = new ArrayList<>();
                for (BaggageClaim baggageClaim : baggageClaimList) {
                    BaggageClaimQRDTO baggageClaimQRDTO = new BaggageClaimQRDTO(baggageClaim.getBaggageClaimCode(),baggageClaim.getType(),baggageClaim.getLocation());
                    baggageClaimDTOList.add(baggageClaimQRDTO);
                }
                qrAllDTO.setBaggageClaim(baggageClaimDTOList);
                break;
            case "gate" :
                List<Gate> gateList = gateRepository.findByisActive("Y");
                List<GateQRDTO> gateQRDTOList = new ArrayList<>();
                for (Gate gate : gateList) {
                    GateQRDTO gateQRDTO = new GateQRDTO(gate.getGateCode(),gate.getLocation(),gate.getType());
                    gateQRDTOList.add(gateQRDTO);
                }
                qrAllDTO.setGate(gateQRDTOList);
                break;
            case "checkin_counter" :
                List<CheckinCounter> checkinCounterList = checkinCounterRepository.findByisActive("Y");
                List<CheckInCounterQRDTO> checkinCounterQRDTOList = new ArrayList<>();
                for (CheckinCounter checkinCounter : checkinCounterList) {
                    CheckInCounterQRDTO checkInCounterQRDTO = new CheckInCounterQRDTO(checkinCounter.getCheckinCounterCode(),checkinCounter.getLocation(),checkinCounter.getType());
                    checkinCounterQRDTOList.add(checkInCounterQRDTO);
                }
                qrAllDTO.setCheckInCounter(checkinCounterQRDTOList);
                break;
            case "store" :
                List<StoreEntity> storeList = storeRepository.findByIsActive("Y");
                List<StoreQRDTO> storeDTOList = new ArrayList<>();
                for (StoreEntity storeEntity : storeList) {
                    StoreQRDTO storeQRDTO = new StoreQRDTO(storeEntity.getStoreId(),storeEntity.getStoreName(),storeEntity.getStoreLocation());
                    storeDTOList.add(storeQRDTO);
                }
                qrAllDTO.setStore(storeDTOList);
                break;
            case "storage" :
                List<StorageEntity> storageList = storageRepository.findByisActive("Y");
                List<StorageQRDTO> storageQRDTOList = new ArrayList<>();
                for (StorageEntity storageEntity : storageList) {
                    StorageQRDTO storageQRDTO = new StorageQRDTO(storageEntity.getStorageCode(),storageEntity.getLocation(),storageEntity.getType());
                    storageQRDTOList.add(storageQRDTO);
                }
                qrAllDTO.setStorage(storageQRDTOList);
                break;
            case "facilities" :
                List<FacilitiesEntity> facilitiesEntityList = facilitiesRepository.findAllByIsActive("Y");
                List<FacilityQRDTO> facilityQRDTOList = new ArrayList<>();
                for (FacilitiesEntity facilitiesEntity : facilitiesEntityList) {
                    FacilityQRDTO facilityQRDTO = new FacilityQRDTO(facilitiesEntity.getFacilitiesCode(),facilitiesEntity.getLocation(),facilitiesEntity.getFacilitiesName());
                    facilityQRDTOList.add(facilityQRDTO);
                }
                qrAllDTO.setFacilities(facilityQRDTOList);
                break;
            case "show_all":
                List<BaggageClaim> allBaggageClaimList = baggageClaimRepository.findByisActive("Y");
                List<BaggageClaimQRDTO> allBaggageClaimDTOList = new ArrayList<>();
                for (BaggageClaim baggageClaim : allBaggageClaimList) {
                    BaggageClaimQRDTO baggageClaimQRDTO = new BaggageClaimQRDTO(
                            baggageClaim.getBaggageClaimCode(),
                            baggageClaim.getType(),
                            baggageClaim.getLocation()
                    );
                    allBaggageClaimDTOList.add(baggageClaimQRDTO);
                }
                qrAllDTO.setBaggageClaim(allBaggageClaimDTOList);

                List<Gate> allGateList = gateRepository.findByisActive("Y");
                List<GateQRDTO> allGateQRDTOList = new ArrayList<>();
                for (Gate gate : allGateList) {
                    GateQRDTO gateQRDTO = new GateQRDTO(
                            gate.getGateCode(),
                            gate.getLocation(),
                            gate.getType()
                    );
                    allGateQRDTOList.add(gateQRDTO);
                }
                qrAllDTO.setGate(allGateQRDTOList);

                List<CheckinCounter> allCheckinCounterList = checkinCounterRepository.findByisActive("Y");
                List<CheckInCounterQRDTO> allCheckinCounterQRDTOList = new ArrayList<>();
                for (CheckinCounter checkinCounter : allCheckinCounterList) {
                    CheckInCounterQRDTO checkInCounterQRDTO = new CheckInCounterQRDTO(
                            checkinCounter.getCheckinCounterCode(),
                            checkinCounter.getLocation(),
                            checkinCounter.getType()
                    );
                    allCheckinCounterQRDTOList.add(checkInCounterQRDTO);
                }
                qrAllDTO.setCheckInCounter(allCheckinCounterQRDTOList);

                List<StoreEntity> allStoreList = storeRepository.findByIsActive("Y");
                List<StoreQRDTO> allStoreDTOList = new ArrayList<>();
                for (StoreEntity storeEntity : allStoreList) {
                    StoreQRDTO storeQRDTO = new StoreQRDTO(
                            storeEntity.getStoreId(),
                            storeEntity.getStoreName(),
                            storeEntity.getStoreLocation()
                    );
                    allStoreDTOList.add(storeQRDTO);
                }
                qrAllDTO.setStore(allStoreDTOList);

                List<StorageEntity> allStorageList = storageRepository.findByisActive("Y");
                List<StorageQRDTO> allStorageQRDTOList = new ArrayList<>();
                for (StorageEntity storageEntity : allStorageList) {
                    StorageQRDTO storageQRDTO = new StorageQRDTO(
                            storageEntity.getStorageCode(),
                            storageEntity.getLocation(),
                            storageEntity.getType()
                    );
                    allStorageQRDTOList.add(storageQRDTO);
                }
                qrAllDTO.setStorage(allStorageQRDTOList);

                List<FacilitiesEntity> allFacilitiesEntityList = facilitiesRepository.findAllByIsActive("Y");
                List<FacilityQRDTO> allFacilityQRDTOList = new ArrayList<>();
                for (FacilitiesEntity facilitiesEntity : allFacilitiesEntityList) {
                    FacilityQRDTO facilityQRDTO = new FacilityQRDTO(
                            facilitiesEntity.getFacilitiesCode(),
                            facilitiesEntity.getLocation(),
                            facilitiesEntity.getFacilitiesName()
                    );
                    allFacilityQRDTOList.add(facilityQRDTO);
                }
                qrAllDTO.setFacilities(allFacilityQRDTOList);
                break;
        }

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "QR코드 생성 시설물 조회성공",qrAllDTO));
    }

    public ResponseEntity<?> getQRDetail(String type, int code) {
        // 위치 가져오기 // store 은 storeEntity 나머지는 location
        FacilityDetailDTO location = new FacilityDetailDTO();
        LocationEntity locationEntity = null;
        switch (type) {
            case "store" :
                StoreEntity store = storeRepository.findById(code).orElse(null);
                location.setLocation(store.getStoreLocation());
                break;
            case "baggage_claim" :
                locationEntity = locationRepository.findByBaggageClaimCode(code);
                location.setLocation(locationEntity.getZone().getRegion() + " " + locationEntity.getZone().getFloor() + " " + locationEntity.getZone().getLocation());
                break;
                case "gate" :
                location.setLocation(code + "번 게이트");
                break;
            case "checkin_counter" :
                locationEntity = locationRepository.findByCheckinCounterCode(code);
                location.setLocation(locationEntity.getZone().getRegion() + " " + locationEntity.getZone().getFloor());
                break;
            case "storage" :
                locationEntity = locationRepository.findByStorageCode(code);
                location.setLocation(locationEntity.getZone().getRegion() + " " + locationEntity.getZone().getFloor());
                break;
            case "facilities" :
                locationEntity = locationRepository.findByFacilitiesCode(code);
                location.setLocation(locationEntity.getZone().getRegion() + " " + locationEntity.getZone().getFloor());
                break;

        }


        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "QR코드 시설물 위 조회성공",location));
    }

    public ResponseEntity<?> joinQR(JoinQRDTO info) {

        // 날짜 형식 정의 및 파싱
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parseDate;
        try {
            parseDate = dateFormat.parse(info.getRegularInspectionDate());
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format.");
        }

        // Builder 패턴을 사용하여 기본 엔티티 생성
        InspectionEntity.InspectionEntityBuilder inspectionEntityBuilder = InspectionEntity.builder()
                .manager(info.getManager())
                .location(info.getLocation())
                .regularInspectionDate(parseDate)
                .type(info.getType())
                .status(info.getStatus())
                .phone(info.getPhone())
                .text(info.getText());

        // Type에 따른 처리
        switch (info.getType()) {
            case "gate":
                Gate gate = gateRepository.findBygateCode(info.getAirplaneCode());
                if (gate == null) {
                    return ResponseEntity.badRequest().body("Gate not found.");
                }
                gateRepository.save(gate);
                inspectionEntityBuilder.gate(gate);
                break;

            case "checkinCounter":
                CheckinCounter checkinCounter = checkinCounterRepository.findBycheckinCounterCode(info.getAirplaneCode());
                if (checkinCounter == null) {
                    return ResponseEntity.badRequest().body("CheckinCounter not found.");
                }
                checkinCounterRepository.save(checkinCounter);
                inspectionEntityBuilder.checkinCounter(checkinCounter);
                break;

            case "baggageClaim":
                BaggageClaim baggageClaim = baggageClaimRepository.findBybaggageClaimCode(info.getAirplaneCode());
                if (baggageClaim == null) {
                    return ResponseEntity.badRequest().body("BaggageClaim not found.");
                }
                baggageClaimRepository.save(baggageClaim);
                inspectionEntityBuilder.baggageClaim(baggageClaim);
                break;

            case "store":
                StoreEntity store = storeRepository.findById(info.getAirplaneCode()).orElse(null);
                if (store == null) {
                    return ResponseEntity.badRequest().body("Store not found.");
                }
                storeRepository.save(store);
                inspectionEntityBuilder.store(store);
                break;

            case "storage":
                StorageEntity storageEntity = storageRepository.findById(info.getAirplaneCode()).orElse(null);
                if (storageEntity == null) {
                    return ResponseEntity.badRequest().body("StorageEntity not found.");
                }
                storageRepository.save(storageEntity);
                inspectionEntityBuilder.storage(storageEntity);
                break;

            case "facilities":
                FacilitiesEntity facilities = facilitiesRepository.findById(info.getAirplaneCode()).orElse(null);
                if (facilities == null) {
                    return ResponseEntity.badRequest().body("FacilitiesEntity not found.");
                }
                facilitiesRepository.save(facilities);
                inspectionEntityBuilder.facilities(facilities);
                break;

            default:
                return ResponseEntity.badRequest().body("Invalid type.");
        }

        InspectionEntity inspectionEntity = inspectionEntityBuilder.build();
        inspectionRepository.save(inspectionEntity);
        return ResponseEntity.ok().build();
    }


}
