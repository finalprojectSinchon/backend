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

import java.util.ArrayList;
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
}
