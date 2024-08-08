
package com.finalproject.airport.approval.service;

import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaim;
import com.finalproject.airport.airplane.baggageclaim.repository.BaggageClaimRepository;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounter;
import com.finalproject.airport.airplane.checkincounter.repository.CheckinCounterRepository;
import com.finalproject.airport.airplane.gate.entity.Gate;
import com.finalproject.airport.airplane.gate.repository.GateRepository;
import com.finalproject.airport.approval.dto.ApprovalDTO;
import com.finalproject.airport.approval.entity.ApprovalEntity;
import com.finalproject.airport.approval.entity.ApprovalStatusEntity;
import com.finalproject.airport.approval.repository.ApprovalRepository;
import com.finalproject.airport.facilities.entity.FacilitiesEntity;
import com.finalproject.airport.facilities.repository.FacilitiesRepository;
import com.finalproject.airport.storage.entity.StorageEntity;
import com.finalproject.airport.storage.repository.StorageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final ModelMapper modelMapper;
    private final GateRepository gateRepository;
    private final CheckinCounterRepository checkinCounterRepository;
    private final BaggageClaimRepository baggageClaimRepository;
    private final StorageRepository storageRepository;
    private final FacilitiesRepository facilitiesRepository;

    @Autowired
    public ApprovalService(ApprovalRepository repository, ModelMapper modelMapper, GateRepository gateRepository, CheckinCounterRepository checkinCounterRepository, BaggageClaimRepository baggageClaimRepository, StorageRepository storageRepository, FacilitiesRepository facilitiesRepository) {
        this.approvalRepository = repository;
        this.modelMapper = modelMapper;
        this.gateRepository = gateRepository;
        this.checkinCounterRepository = checkinCounterRepository;
        this.baggageClaimRepository = baggageClaimRepository;
        this.storageRepository = storageRepository;
        this.facilitiesRepository = facilitiesRepository;
    }

    public List<ApprovalEntity> findAll() {
        return approvalRepository.findAll();
    }

    @Transactional
    public void saveGateApproval(ApprovalDTO approvalDTO) {
        System.out.println("approval: " + approvalDTO);
        Gate gate = gateRepository.findBygateCode(approvalDTO.getGateCode());

        ApprovalEntity approvalEntity = new ApprovalEntity(
                approvalDTO.getType(),
                approvalDTO.getStatus(),
                gate,
                null,
                null,
                null,
                null
        );

        System.out.println("approvalEntity: " + approvalEntity);
        approvalRepository.save(approvalEntity);

    }

    @Transactional
    public void saveChkinCounterApproval(ApprovalDTO approvalDTO) {
        System.out.println("approval: " + approvalDTO);
        CheckinCounter checkinCounter = checkinCounterRepository.findBycheckinCounterCode(approvalDTO.getCheckinCounterCode());

        ApprovalEntity approvalEntity = new ApprovalEntity(
                approvalDTO.getType(),
                approvalDTO.getStatus(),
                null,
                checkinCounter,
                null,
                null,
                null
        );

        System.out.println("approvalEntity: " + approvalEntity);
        approvalRepository.save(approvalEntity);
    }

    @Transactional
    public void saveBaggageClaimApproval(ApprovalDTO approvalDTO) {

        BaggageClaim baggageClaim = baggageClaimRepository.findBybaggageClaimCode(approvalDTO.getBaggageClaimCode());
        ApprovalEntity approvalEntity = new ApprovalEntity(
                approvalDTO.getType(),
                approvalDTO.getStatus(),
                null,
                null,
                baggageClaim,
                null,
                null

        );

        System.out.println("approvalEntity: " + approvalEntity);
        approvalRepository.save(approvalEntity);
    }

    @Transactional
    public void saveStorageApproval(ApprovalDTO approvalDTO){

        StorageEntity storage = storageRepository.findBystorageCode(approvalDTO.getStorageCode());
        ApprovalEntity approvalEntity = new ApprovalEntity(
                approvalDTO.getType(),
                approvalDTO.getStatus(),
                null,
                null,
                null,
                storage,
                null
        );

        System.out.println("approvalEntity = " + approvalEntity);
        approvalRepository.save(approvalEntity);
    }

    @Transactional
    public void saveFacilities(ApprovalDTO approvalDTO){

        FacilitiesEntity facilities = facilitiesRepository.findByfacilitiesCode( approvalDTO.getFacilitiesCode());
        ApprovalEntity approvalEntity = new ApprovalEntity(
                approvalDTO.getType(),
                approvalDTO.getStatus(),
                null,
                null,
                null,
                null,
                facilities
        );
        System.out.println("approvalEntity = " + approvalEntity);
        approvalRepository.save(approvalEntity);
    }

    private ApprovalEntity approveCommon(Integer approvalCode) {
        if (approvalCode == null) {
            throw new IllegalArgumentException("Approval code must not be null");
        }

        // 승인 엔티티 조회
        Optional<ApprovalEntity> approvalEntityOptional = approvalRepository.findById(approvalCode);
        if (approvalEntityOptional.isPresent()) {
            ApprovalEntity approvalEntity = approvalEntityOptional.get();

            // approval_status를 N에서 Y로 변경
            approvalEntity.setApprovalStatus(ApprovalStatusEntity.Y);
            approvalRepository.save(approvalEntity);

            return approvalEntity;
        } else {
            throw new RuntimeException("Approval not found: " + approvalCode);
        }
    }

    @Transactional
    public void approveGate(Integer approvalCode) {
        ApprovalEntity approvalEntity = approveCommon(approvalCode);

        Integer gateCode = approvalEntity.getGate().getGateCode();
        if (gateCode == null) {
            throw new IllegalArgumentException("Gate code must not be null");
        }

        // 게이트 엔티티 조회 및 수정
        Optional<Gate> gateEntityOptional = gateRepository.findById(gateCode);
        if (gateEntityOptional.isPresent()) {
            Gate gate = gateEntityOptional.get();
            if ("N".equals(gate.getIsActive())) { // 현재 상태가 N인 경우에만 변경
                gate.setIsActive("Y"); // isActive를 N에서 Y로 변경
                gateRepository.save(gate);
            }
        } else {
            throw new RuntimeException("Gate not found: " + gateCode);
        }
    }

    @Transactional
    public void approveCheckInCounter(Integer approvalCode) {
        ApprovalEntity approvalEntity = approveCommon(approvalCode);

        Integer checkinCounterCode = approvalEntity.getCheckinCounter().getCheckinCounterCode();
        if (checkinCounterCode == null) {
            throw new IllegalArgumentException("CheckinCounter code must not be null");
        }

        // 체크인 카운터 엔티티 조회 및 수정
        Optional<CheckinCounter> checkinCounterEntityOptional = checkinCounterRepository.findById(checkinCounterCode);
        if (checkinCounterEntityOptional.isPresent()) {
            CheckinCounter checkinCounter = checkinCounterEntityOptional.get();
            if ("N".equals(checkinCounter.getIsActive())) { // 현재 상태가 N인 경우에만 변경
                checkinCounter.setIsActive("Y"); // isActive를 N에서 Y로 변경
                checkinCounterRepository.save(checkinCounter);
            }
        } else {
            throw new RuntimeException("CheckinCounter not found: " + checkinCounterCode);
        }
    }

//    @Transactional
//    public void approveBaggageClaim(Integer approvalCode) {
//        ApprovalEntity approvalEntity = approveCommon(approvalCode);
//
//        Integer originalBaggageClaimCode = approvalEntity.getOriginalBaggageClaimCode();
//        if (originalBaggageClaimCode == null) {
//            throw new IllegalArgumentException("Original Baggage claim code must not be null");
//        }
//
//        // 원래 수하물 수취대 엔티티 조회 및 수정
//        Optional<BaggageClaim> originalBaggageClaimEntityOptional = baggageClaimRepository.findById(originalBaggageClaimCode);
//        if (originalBaggageClaimEntityOptional.isPresent()) {
//            BaggageClaim originalBaggageClaim = originalBaggageClaimEntityOptional.get();
//            originalBaggageClaim.setIsActive("N");
//            baggageClaimRepository.save(originalBaggageClaim);
//        } else {
//            throw new RuntimeException("Original Baggage claim not found: " + originalBaggageClaimCode);
//        }
//
//        // 수정된 수하물 수취대 엔티티 조회 및 수정
//        Integer modifiedBaggageClaimCode = approvalEntity.getBaggageClaim().getBaggageClaimCode();
//        if (modifiedBaggageClaimCode == null) {
//            throw new IllegalArgumentException("Modified Baggage claim code must not be null");
//        }
//
//        Optional<BaggageClaim> modifiedBaggageClaimEntityOptional = baggageClaimRepository.findById(modifiedBaggageClaimCode);
//        if (modifiedBaggageClaimEntityOptional.isPresent()) {
//            BaggageClaim modifiedBaggageClaim = modifiedBaggageClaimEntityOptional.get();
//            if ("N".equals(modifiedBaggageClaim.getIsActive())) {
//                modifiedBaggageClaim.setIsActive("Y");
//                baggageClaimRepository.save(modifiedBaggageClaim);
//            }
//        } else {
//            throw new RuntimeException("Modified Baggage claim not found: " + modifiedBaggageClaimCode);
//        }
//    }

    @Transactional
    public void approveBaggageClaim(Integer approvalCode) {
        // 공통 승인 로직 호출
        ApprovalEntity approvalEntity = approveCommon(approvalCode);

        // 원래 수하물 수취대 코드 가져오기
        Integer originalBaggageClaimCode = approvalEntity.getCode();

        // 원래 수하물 수취대 엔티티 조회 및 수정
        BaggageClaim originalBaggageClaimEntityOptional = baggageClaimRepository.findBybaggageClaimCode(originalBaggageClaimCode);
        originalBaggageClaimEntityOptional = originalBaggageClaimEntityOptional.toBuilder().isActive("N").build();
        baggageClaimRepository.save(originalBaggageClaimEntityOptional);

        // 수정된 수하물 수취대 코드 가져오기
        Integer modifiedBaggageClaimCode = approvalEntity.getBaggageClaim().getBaggageClaimCode();

        // 수정된 수하물 수취대 엔티티 조회 및 수정
        BaggageClaim modifiedBaggageClaimEntityOptional = baggageClaimRepository.findBybaggageClaimCode(modifiedBaggageClaimCode);
        modifiedBaggageClaimEntityOptional = modifiedBaggageClaimEntityOptional.toBuilder().isActive("Y").build();
        baggageClaimRepository.save(modifiedBaggageClaimEntityOptional);
    }





    @Transactional
    public void approveStorage(Integer approvalCode){
        ApprovalEntity approvalEntity = approveCommon(approvalCode);

        Integer storageCode = approvalEntity.getStorage().getStorageCode();
        if(storageCode == null){
            throw new IllegalArgumentException("Storage code must not be null");
        }

        Optional<StorageEntity> storageEntityOptional = storageRepository.findById(storageCode);
        if(storageEntityOptional.isPresent()){
            StorageEntity storageEntity = storageEntityOptional.get();
            if("N".equals(storageEntity.getIsActive())){
                storageEntity.setIsActive("Y");
                storageRepository.save(storageEntity);
            }
        }else {
            throw new RuntimeException("Storage not found:" + storageCode);
        }
    }


    //    public void approveFacilities(Integer approvalCode) {
//        ApprovalEntity approvalEntity = approveCommon(approvalCode);
//
//        Integer facilitiesCode = approvalEntity.getFacilities().getFacilitiesCode();
//        if(facilitiesCode == null){
//            throw new IllegalArgumentException("Facilities code must not be null");
//        }
//
//        Optional<FacilitiesEntity> facilitiesEntityOptional = facilitiesRepository.findById(facilitiesCode);
//        if(facilitiesEntityOptional.isPresent()){
//            FacilitiesEntity facilitiesEntity = facilitiesEntityOptional.get();
//            if ("N".equals(facilitiesEntity.getIsActive())) {
//                facilitiesEntity.setIsActive("Y");
//                facilitiesRepository.save(facilitiesEntity);
//            }
//        }else {
//            throw new RuntimeException("Facilities not found: " + facilitiesCode);
//        }
//    }
    @Transactional
    public void approveFacilities(Integer approvalCode) {
        ApprovalEntity approvalEntity = approveCommon(approvalCode);

        // 시설 코드 확인 전에 null 체크
        FacilitiesEntity facilities = approvalEntity.getFacilities();
        if (facilities != null) {
            Integer facilitiesCode = facilities.getFacilitiesCode();

            // 원래 시설 엔티티 조회 및 비활성화
            Optional<FacilitiesEntity> originalFacilitiesOptional = facilitiesRepository.findById(facilitiesCode);
            if (originalFacilitiesOptional.isPresent()) {
                FacilitiesEntity originalFacilities = originalFacilitiesOptional.get();
                originalFacilities.setIsActive("N");
                facilitiesRepository.save(originalFacilities);
            } else {
                throw new RuntimeException("Original Facilities not found: " + facilitiesCode);
            }

            // 수정된 시설 엔티티 조회 및 활성화
            Integer modifiedFacilitiesCode = approvalEntity.getFacilities().getFacilitiesCode();
            if (modifiedFacilitiesCode == null) {
                throw new IllegalArgumentException("Modified Facilities code must not be null");
            }

            Optional<FacilitiesEntity> modifiedFacilitiesOptional = facilitiesRepository.findById(modifiedFacilitiesCode);
            if (modifiedFacilitiesOptional.isPresent()) {
                FacilitiesEntity modifiedFacilities = modifiedFacilitiesOptional.get();
                modifiedFacilities.setIsActive("Y");
                facilitiesRepository.save(modifiedFacilities);
            } else {
                throw new RuntimeException("Modified Facilities not found: " + modifiedFacilitiesCode);
            }
        }
    }

    // 수정



}
