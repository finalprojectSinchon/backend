
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

        Integer originalGateCode = approvalEntity.getCode();

        //원래 체크인 카운터 조회 및 수정
        Gate originalGateEntityOptional = gateRepository.findBygateCode(originalGateCode);
        originalGateEntityOptional = originalGateEntityOptional.toBuilder().isActive("N").build();
        gateRepository.save(originalGateEntityOptional);

        //수정된 것 가지고 오기
        Integer modifiedGateCode = approvalEntity.getGate().getGateCode();

        Gate modifiedGateCodeOptional = gateRepository.findBygateCode(modifiedGateCode);
        modifiedGateCodeOptional = modifiedGateCodeOptional.toBuilder().isActive("Y").build();
        gateRepository.save(modifiedGateCodeOptional);
    }

    @Transactional
    public void approveCheckInCounter(Integer approvalCode) {
        ApprovalEntity approvalEntity = approveCommon(approvalCode);

        //원해 체크인 카운터 코드 가져오기
        Integer originalCheckInCounterCode = approvalEntity.getCode();

        //원래 체크인 카운터 조회 및 수정
        CheckinCounter originalCheckinCounterEntityOptional = checkinCounterRepository.findBycheckinCounterCode(originalCheckInCounterCode);
        originalCheckinCounterEntityOptional = originalCheckinCounterEntityOptional.toBuilder().isActive("N").build();
        checkinCounterRepository.save(originalCheckinCounterEntityOptional);

        //수정된 것 가지고 오기
        Integer modifiedCheckInCounterCode = approvalEntity.getCheckinCounter().getCheckinCounterCode();

        CheckinCounter modifiedCheckInCounterCodeOptional = checkinCounterRepository.findBycheckinCounterCode(modifiedCheckInCounterCode);
        modifiedCheckInCounterCodeOptional = modifiedCheckInCounterCodeOptional.toBuilder().isActive("Y").build();
        checkinCounterRepository.save(modifiedCheckInCounterCodeOptional);
    }


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


    public void notiChecked(int approveCode) {

        ApprovalEntity approval = approvalRepository.findByApprovalCode(approveCode);

        approval = approval.toBuilder().checked("Y").build();
        approvalRepository.save(approval);

    }

}
