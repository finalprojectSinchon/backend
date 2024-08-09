
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
import com.finalproject.airport.store.entity.StoreEntity;
import com.finalproject.airport.store.repository.StoreRepository;
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
    private final StoreRepository storeRepository;

    @Autowired
    public ApprovalService(ApprovalRepository repository, ModelMapper modelMapper, GateRepository gateRepository, CheckinCounterRepository checkinCounterRepository, BaggageClaimRepository baggageClaimRepository, StorageRepository storageRepository, FacilitiesRepository facilitiesRepository, StoreRepository storeRepository) {
        this.approvalRepository = repository;
        this.modelMapper = modelMapper;
        this.gateRepository = gateRepository;
        this.checkinCounterRepository = checkinCounterRepository;
        this.baggageClaimRepository = baggageClaimRepository;
        this.storageRepository = storageRepository;
        this.facilitiesRepository = facilitiesRepository;
        this.storeRepository = storeRepository;
    }

    public List<ApprovalEntity> findAll() {
        return approvalRepository.findAll();
    }

    @Transactional
    public void saveGateApproval(ApprovalEntity approval) {

        Gate gate = gateRepository.findBygateCode(approval.getGate().getGateCode());

        gate = gate.toBuilder().isActive("Y").build();
        gateRepository.save(gate);

        approval = approval.toBuilder().status("Y").build();
        approvalRepository.save(approval);

    }

    @Transactional
    public void saveChkinCounterApproval(ApprovalEntity approval) {

        System.out.println("didididididi = " + approval);

        CheckinCounter checkinCounter = checkinCounterRepository.findBycheckinCounterCode(approval.getCheckinCounter().getCheckinCounterCode());
        checkinCounter = checkinCounter.toBuilder().isActive("Y").build();
        checkinCounterRepository.save(checkinCounter);

        approval = approval.toBuilder().status("Y").build();
        approvalRepository.save(approval);
    }

    @Transactional
    public void saveBaggageClaimApproval(ApprovalEntity approval) {

        BaggageClaim baggageClaim = baggageClaimRepository.findBybaggageClaimCode(approval.getBaggageClaim().getBaggageClaimCode());
        baggageClaim = baggageClaim.toBuilder().isActive("Y").build();
        baggageClaimRepository.save(baggageClaim);
        approval = approval.toBuilder().status("Y").build();
        approvalRepository.save(approval);


    }

    @Transactional
    public void saveStorageApproval(ApprovalEntity approval){

        StorageEntity storage = storageRepository.findBystorageCode(approval.getStorage().getStorageCode());
        storage = storage.toBuilder().isActive("Y").build();
        storageRepository.save(storage);
        approval = approval.toBuilder().status("Y").build();
        approvalRepository.save(approval);

    }

    @Transactional
    public void saveFacilities(ApprovalEntity approval){

        FacilitiesEntity facilities = facilitiesRepository.findByfacilitiesCode( approval.getFacilities().getFacilitiesCode() );
        ApprovalEntity approvalEntity = new ApprovalEntity(
                approval.getType(),
                approval.getStatus(),
                null,
                null,
                null,
                null,
                facilities
        );
        System.out.println("approvalEntity = " + approvalEntity);
        approvalRepository.save(approvalEntity);
    }

    public ApprovalEntity approveCommon(Integer approvalCode) {
        if (approvalCode == null) {
            throw new IllegalArgumentException("Approval code must not be null");
        }

        // 승인 엔티티 조회
        Optional<ApprovalEntity> approvalEntityOptional = approvalRepository.findById(approvalCode);
        if (approvalEntityOptional.isPresent()) {
            ApprovalEntity approvalEntity = approvalEntityOptional.get();

            // approval_status를 N에서 Y로 변경
            approvalEntity = approvalEntity.toBuilder().status("Y").build();
            approvalRepository.save(approvalEntity);

            return approvalEntity;
        } else {
            throw new RuntimeException("Approval not found: " + approvalCode);
        }
    }

    @Transactional
    public void approveGate(Integer approvalCode) {
        ApprovalEntity approvalEntity = approveCommon(approvalCode);

        approvalEntity = approvalEntity.toBuilder().status("Y").build();
        approvalRepository.save(approvalEntity);
    }

    @Transactional
    public void approveBaggageClaim(Integer approvalCode) {
        // 공통 승인 로직 호출
        ApprovalEntity approvalEntity = approveCommon(approvalCode);

        approvalEntity = approvalEntity.toBuilder().status("Y").build();
        approvalRepository.save(approvalEntity);

    }

    @Transactional
    public void approveCheckInCounter(Integer approvalCode) {
        ApprovalEntity approvalEntity = approveCommon(approvalCode);

        approvalEntity = approvalEntity.toBuilder().status("Y").build();
        approvalRepository.save(approvalEntity);
    }

    @Transactional
    public void registApproveCheckInCounter(Integer approvalCode) {
        ApprovalEntity approvalEntity = approveCommon(approvalCode);

        //원래 체크인 카운터 조회 및 수정
        CheckinCounter checkinCounter = checkinCounterRepository.findBycheckinCounterCode(approvalEntity.getCheckinCounter().getCheckinCounterCode());
        checkinCounter = checkinCounter.toBuilder().isActive("Y").build();
        checkinCounterRepository.save(checkinCounter);

    }




    @Transactional
    public void approveFacilities(Integer approvalCode) {
        ApprovalEntity approvalEntity = approveCommon(approvalCode);
        System.out.println("approvalEntity = " + approvalEntity);

        // 원래 편의시설 코드 가져오기
        Integer originalFacilitiesCode = approvalEntity.getCode();
        System.out.println("originalFacilitiesCode = " + originalFacilitiesCode);

        // 원래 편의시설 엔티티 조회 및 수정
        FacilitiesEntity originalFacilitiesEntityOptional = facilitiesRepository.findByfacilitiesCode(originalFacilitiesCode);
        System.out.println("originalFacilitiesEntityOptional = " + originalFacilitiesEntityOptional);
        originalFacilitiesEntityOptional = originalFacilitiesEntityOptional.toBuilder().isActive("N").build();
        facilitiesRepository.save(originalFacilitiesEntityOptional);

        // 수정된 편의시설 코드 가져오기
        Integer modifiedFacilitiesCode = approvalEntity.getFacilities().getFacilitiesCode();

        // 수정된 편의시설 엔티티 조회 및 수정
        FacilitiesEntity modifiedFacilitiesEntityOptional = facilitiesRepository.findByfacilitiesCode(modifiedFacilitiesCode);
        modifiedFacilitiesEntityOptional = modifiedFacilitiesEntityOptional.toBuilder().isActive("Y").build();
        facilitiesRepository.save(modifiedFacilitiesEntityOptional);
    }


    @Transactional
    public void approveStorage(Integer approvalCode){
        ApprovalEntity approvalEntity = approveCommon(approvalCode);

        Integer originalStorageCode = approvalEntity.getCode();
        System.out.println("originalStorageCode = " + originalStorageCode);

        // 원래 창고 엔티티 조회 및 수정
        StorageEntity originalStorageEntityOptional = storageRepository.findBystorageCode(originalStorageCode);
        System.out.println("originalFacilitiesEntityOptional = " + originalStorageEntityOptional);
        originalStorageEntityOptional = originalStorageEntityOptional.toBuilder().isActive("N").build();
        storageRepository.save(originalStorageEntityOptional);

        // 수정된 창고 코드 가져오기
        Integer modifiedStorageCode = approvalEntity.getStorage().getStorageCode();

        // 수정된 창고 엔티티 조회 및 수정
        StorageEntity modifiedStorageEntityOptional = storageRepository.findBystorageCode(modifiedStorageCode);
        modifiedStorageEntityOptional = modifiedStorageEntityOptional.toBuilder().isActive("Y").build();
        storageRepository.save(modifiedStorageEntityOptional);
    }


    @Transactional
    public void approveStore(Integer approvalCode){
        ApprovalEntity approvalEntity = approveCommon(approvalCode);

        Integer originalStoreCode = approvalEntity.getCode();

        // 원래 창고 엔티티 조회 및 수정
        StoreEntity originalStoreEntityOptional = storeRepository.findBystoreId(originalStoreCode);
        System.out.println("originalStoreEntityOptional = " + originalStoreEntityOptional);
        originalStoreEntityOptional = originalStoreEntityOptional.toBuilder().isActive("N").build();
        storeRepository.save(originalStoreEntityOptional);

        // 수정된 창고 코드 가져오기
        Integer modifiedStoreCode = approvalEntity.getStore().getStoreId();

        // 수정된 수하물 수취대 엔티티 조회 및 수정
        StoreEntity modifiedStoreEntityOptional = storeRepository.findBystoreId(modifiedStoreCode);
        modifiedStoreEntityOptional = modifiedStoreEntityOptional.toBuilder().isActive("Y").build();
        storeRepository.save(modifiedStoreEntityOptional);
    }

    public void notiChecked(int approveCode) {

        ApprovalEntity approval = approvalRepository.findByApprovalCode(approveCode);

        approval = approval.toBuilder().checked("Y").build();
        approvalRepository.save(approval);

    }




}
