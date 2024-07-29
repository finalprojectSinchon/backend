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

    @Autowired
    public ApprovalService(ApprovalRepository repository, ModelMapper modelMapper, GateRepository gateRepository, CheckinCounterRepository checkinCounterRepository, BaggageClaimRepository baggageClaimRepository, StorageRepository storageRepository) {
        this.approvalRepository = repository;
        this.modelMapper = modelMapper;
        this.gateRepository = gateRepository;
        this.checkinCounterRepository = checkinCounterRepository;
        this.baggageClaimRepository = baggageClaimRepository;
        this.storageRepository = storageRepository;
    }

    public List<ApprovalEntity> findAll() {
        return approvalRepository.findAll();
    }

    @Transactional
    public void saveGateApproval(ApprovalDTO approvalDTO) {
        System.out.println("approval: " + approvalDTO);
        

        ApprovalEntity approvalEntity = new ApprovalEntity(
                approvalDTO.getApprovalType(),
                approvalDTO.getApprovalStatus(),
                approvalDTO.getGateCode(),
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


        ApprovalEntity approvalEntity = new ApprovalEntity(
                approvalDTO.getApprovalType(),
                approvalDTO.getApprovalStatus(),
                null,
                approvalDTO.getCheckinCounterCode(),
                null,
                null
        );

        System.out.println("approvalEntity: " + approvalEntity);
        approvalRepository.save(approvalEntity);
    }

    @Transactional
    public void saveBaggageClaimApproval(ApprovalDTO approvalDTO) {
        ApprovalEntity approvalEntity = new ApprovalEntity(
                approvalDTO.getApprovalType(),
                approvalDTO.getApprovalStatus(),
                null,
                null,
                null,
                approvalDTO.getBaggageClaimCode());

        System.out.println("approvalEntity: " + approvalEntity);
        approvalRepository.save(approvalEntity);
    }

    @Transactional
    public void saveStorageApproval(ApprovalDTO approvalDTO){
        ApprovalEntity approvalEntity = new ApprovalEntity(
                approvalDTO.getApprovalType(),
                approvalDTO.getApprovalStatus(),
                null,
                null,
                null,
                approvalDTO.getStorageCode());

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

        Integer gateCode = approvalEntity.getGate();
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

        Integer checkinCounterCode = approvalEntity.getCheckinCounter();
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

    @Transactional
    public void approveBaggageClaim(Integer approvalCode) {
        ApprovalEntity approvalEntity = approveCommon(approvalCode);

        Integer baggageClaimCode = approvalEntity.getBaggageClaim();
        if (baggageClaimCode == null) {
            throw new IllegalArgumentException("Baggage claim code must not be null");
        }

        //수하물 수취대 엔티티 조회 및 수정
        Optional<BaggageClaim> baggageClaimEntityOptional = baggageClaimRepository.findById(baggageClaimCode);
        if (baggageClaimEntityOptional.isPresent()) {
            BaggageClaim baggageClaim = baggageClaimEntityOptional.get();
            if ("N".equals(baggageClaim.getIsActive())) {
                baggageClaim.setIsActive("Y");
                baggageClaimRepository.save(baggageClaim);
            }
        } else {
            throw new RuntimeException("Baggage claim not found: " + baggageClaimCode);
        }
    }

    @Transactional
    public void approveStorage(Integer approvalCode){
        ApprovalEntity approvalEntity = approveCommon(approvalCode);

        Integer storageCode = approvalEntity.getStorage();
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




}
