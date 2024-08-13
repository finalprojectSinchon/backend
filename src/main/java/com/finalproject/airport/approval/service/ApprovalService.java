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
import com.finalproject.airport.manager.entity.ManagersEntity;
import com.finalproject.airport.manager.repository.ManagersRepository;
import com.finalproject.airport.storage.entity.StorageEntity;
import com.finalproject.airport.storage.repository.StorageRepository;
import com.finalproject.airport.store.entity.StoreEntity;
import com.finalproject.airport.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final ModelMapper modelMapper;
    private final GateRepository gateRepository;
    private final CheckinCounterRepository checkinCounterRepository;
    private final BaggageClaimRepository baggageClaimRepository;
    private final StorageRepository storageRepository;
    private final FacilitiesRepository facilitiesRepository;
    private final StoreRepository storeRepository;
    private final ManagersRepository managersRepository;

    public List<ApprovalEntity> findAll() {
        return approvalRepository.findAll();
    }

    @Transactional
    public void saveGateApproval(ApprovalEntity approval) {
        log.info("Saving gate approval: {}", approval);

        Gate gate = gateRepository.findBygateCode(approval.getGate().getGateCode());
        gate = gate.toBuilder().isActive("Y").build();
        gateRepository.save(gate);

        approval = approval.toBuilder().status("Y").build();
        approvalRepository.save(approval);
    }

    @Transactional
    public void saveCheckinCounterApproval(ApprovalEntity approval) {
        log.info("Saving check-in counter approval: {}", approval);

        CheckinCounter checkinCounter = checkinCounterRepository.findBycheckinCounterCode(approval.getCheckinCounter().getCheckinCounterCode());
        checkinCounter = checkinCounter.toBuilder().isActive("Y").build();
        checkinCounterRepository.save(checkinCounter);

        approval = approval.toBuilder().status("Y").build();
        approvalRepository.save(approval);
    }

    @Transactional
    public void saveBaggageClaimApproval(ApprovalEntity approval) {
        log.info("Saving baggage claim approval: {}", approval);

        BaggageClaim baggageClaim = baggageClaimRepository.findBybaggageClaimCode(approval.getBaggageClaim().getBaggageClaimCode());
        baggageClaim = baggageClaim.toBuilder().isActive("Y").build();
        baggageClaimRepository.save(baggageClaim);

        approval = approval.toBuilder().status("Y").build();
        approvalRepository.save(approval);
    }

    @Transactional
    public void saveStorageApproval(ApprovalEntity approval) {
        log.info("Saving storage approval: {}", approval);

        StorageEntity storage = storageRepository.findBystorageCode(approval.getStorage().getStorageCode());
        storage = storage.toBuilder().isActive("Y").build();
        storageRepository.save(storage);

        approval = approval.toBuilder().status("Y").build();
        approvalRepository.save(approval);
    }

    @Transactional
    public void saveFacilitiesApproval(ApprovalEntity approval) {
        log.info("Saving facilities approval: {}", approval);

        FacilitiesEntity facilities = facilitiesRepository.findByfacilitiesCode(approval.getFacilities().getFacilitiesCode());
        facilities = facilities.toBuilder().isActive("Y").build();
        facilitiesRepository.save(facilities);

        approval = approval.toBuilder().status("Y").build();
        approvalRepository.save(approval);

    }

    public ApprovalEntity approveCommon(Integer approvalCode) {
        if (approvalCode == null) {
            throw new IllegalArgumentException("Approval code must not be null");
        }

        log.info("Approving common entity with code: {}", approvalCode);

        Optional<ApprovalEntity> approvalEntityOptional = approvalRepository.findById(approvalCode);
        if (approvalEntityOptional.isPresent()) {
            ApprovalEntity approvalEntity = approvalEntityOptional.get();
            approvalEntity = approvalEntity.toBuilder().status("Y").build();
            approvalRepository.save(approvalEntity);
            return approvalEntity;
        } else {
            throw new RuntimeException("Approval not found: " + approvalCode);
        }
    }

    @Transactional
    public void approveGate(Integer approvalCode) {
        log.info("Approving gate with code: {}", approvalCode);
        ApprovalEntity approvalEntity = approveCommon(approvalCode);
        approvalEntity = approvalEntity.toBuilder().status("Y").build();
        approvalRepository.save(approvalEntity);

    }

    @Transactional
    public void approveBaggageClaim(Integer approvalCode) {
        log.info("Approving baggage claim with code: {}", approvalCode);
        ApprovalEntity approvalEntity = approveCommon(approvalCode);
        approvalEntity = approvalEntity.toBuilder().status("Y").build();
        approvalRepository.save(approvalEntity);
    }

    @Transactional
    public void approveCheckinCounter(Integer approvalCode) {
        log.info("Approving check-in counter with code: {}", approvalCode);
        ApprovalEntity approvalEntity = approveCommon(approvalCode);
        approvalEntity = approvalEntity.toBuilder().status("Y").build();
        approvalRepository.save(approvalEntity);
    }

    @Transactional
    public void registApproveCheckinCounter(Integer approvalCode) {
        log.info("Registering approval for check-in counter with code: {}", approvalCode);
        ApprovalEntity approvalEntity = approveCommon(approvalCode);

        CheckinCounter checkinCounter = checkinCounterRepository.findBycheckinCounterCode(approvalEntity.getCheckinCounter().getCheckinCounterCode());
        checkinCounter = checkinCounter.toBuilder().isActive("Y").build();
        checkinCounterRepository.save(checkinCounter);
    }

    @Transactional
    public void approveFacilities(Integer approvalCode) {
        log.info("Approving facilities with code: {}", approvalCode);
        ApprovalEntity approvalEntity = approveCommon(approvalCode);

        Integer originalFacilitiesCode = approvalEntity.getCode();
        log.info("Original facilities code: {}", originalFacilitiesCode);

        FacilitiesEntity originalFacilitiesEntity = facilitiesRepository.findByfacilitiesCode(originalFacilitiesCode);
        log.info("Original facilities entity: {}", originalFacilitiesEntity);
        originalFacilitiesEntity = originalFacilitiesEntity.toBuilder().isActive("N").build();
        facilitiesRepository.save(originalFacilitiesEntity);

        Integer modifiedFacilitiesCode = approvalEntity.getFacilities().getFacilitiesCode();
        FacilitiesEntity modifiedFacilitiesEntity = facilitiesRepository.findByfacilitiesCode(modifiedFacilitiesCode);
        modifiedFacilitiesEntity = modifiedFacilitiesEntity.toBuilder().isActive("Y").build();
        List<ManagersEntity> managersEntity = managersRepository.findAllByFacilitiesCodeAndIsActive(originalFacilitiesCode,"Y");
        for(ManagersEntity managersEntity1 : managersEntity) {
            managersEntity1.setIsActive("N");
            managersRepository.save(managersEntity1);
        }
        for(ManagersEntity managerEntity : managersEntity) {
            ManagersEntity managersEntity2 = new ManagersEntity();
            managersEntity2 = managersEntity2.toBuilder()
                    .facilitiesCode(modifiedFacilitiesCode)
                    .user(managerEntity.getUser())
                    .build();
            managersRepository.save(managersEntity2);
        }

        facilitiesRepository.save(modifiedFacilitiesEntity);
    }

    @Transactional
    public void approveStorage(Integer approvalCode) {
        log.info("Approving storage with code: {}", approvalCode);
        ApprovalEntity approvalEntity = approveCommon(approvalCode);

        Integer originalStorageCode = approvalEntity.getCode();
        log.info("Original storage code: {}", originalStorageCode);

        StorageEntity originalStorageEntity = storageRepository.findBystorageCode(originalStorageCode);
        log.info("Original storage entity: {}", originalStorageEntity);
        originalStorageEntity = originalStorageEntity.toBuilder().isActive("N").build();
        storageRepository.save(originalStorageEntity);

        Integer modifiedStorageCode = approvalEntity.getStorage().getStorageCode();
        StorageEntity modifiedStorageEntity = storageRepository.findBystorageCode(modifiedStorageCode);
        modifiedStorageEntity = modifiedStorageEntity.toBuilder().isActive("Y").build();

        List<ManagersEntity> managersEntity = managersRepository.findAllByStorageCodeAndIsActive(originalStorageCode,"Y");
        for(ManagersEntity managersEntity1 : managersEntity) {
            managersEntity1.setIsActive("N");
            managersRepository.save(managersEntity1);
        }
        for(ManagersEntity managerEntity : managersEntity) {
            ManagersEntity managersEntity2 = new ManagersEntity();
            managersEntity2 = managersEntity2.toBuilder()
                    .facilitiesCode(modifiedStorageCode)
                    .user(managerEntity.getUser())
                    .build();
            managersRepository.save(managersEntity2);
        }


        storageRepository.save(modifiedStorageEntity);


    }

    @Transactional
    public void approveStore(Integer approvalCode) {
        log.info("Approving store with code: {}", approvalCode);
        ApprovalEntity approvalEntity = approveCommon(approvalCode);

        Integer originalStoreCode = approvalEntity.getCode();
        log.info("Original store code: {}", originalStoreCode);

        StoreEntity originalStoreEntity = storeRepository.findBystoreId(originalStoreCode);
        log.info("Original store entity: {}", originalStoreEntity);
        originalStoreEntity = originalStoreEntity.toBuilder().isActive("N").build();
        storeRepository.save(originalStoreEntity);

        Integer modifiedStoreCode = approvalEntity.getStore().getStoreId();
        StoreEntity modifiedStoreEntity = storeRepository.findBystoreId(modifiedStoreCode);
        modifiedStoreEntity = modifiedStoreEntity.toBuilder().isActive("Y").build();

        List<ManagersEntity> managersEntity = managersRepository.findAllByStoreIdAndIsActive(originalStoreCode,"Y");
        for(ManagersEntity managersEntity1 : managersEntity) {
            managersEntity1.setIsActive("N");
            managersRepository.save(managersEntity1);
        }
        for(ManagersEntity managerEntity : managersEntity) {
            ManagersEntity managersEntity2 = new ManagersEntity();
            managersEntity2 = managersEntity2.toBuilder()
                    .facilitiesCode(modifiedStoreCode)
                    .user(managerEntity.getUser())
                    .build();
            managersRepository.save(managersEntity2);
        }

        storeRepository.save(modifiedStoreEntity);
    }

    public void notiChecked(int approveCode) {
        log.info("Updating notification status for approval code: {}", approveCode);

        ApprovalEntity approval = approvalRepository.findByApprovalCode(approveCode);
        approval = approval.toBuilder().checked("Y").build();
        approvalRepository.save(approval);
    }
}
