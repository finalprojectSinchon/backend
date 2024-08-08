package com.finalproject.airport.airplane.baggageclaim.service;


import com.finalproject.airport.airplane.airplane.Entity.Airplane;
import com.finalproject.airport.airplane.airplane.repository.AirplaneRepository;
import com.finalproject.airport.airplane.baggageclaim.dto.BaggageClaimDTO;
import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaim;
import com.finalproject.airport.airplane.baggageclaim.repository.BaggageClaimRepository;
import com.finalproject.airport.approval.dto.ApprovalDTO;
import com.finalproject.airport.approval.entity.ApprovalEntity;
import com.finalproject.airport.approval.entity.ApprovalStatusEntity;
import com.finalproject.airport.approval.entity.ApprovalTypeEntity;
import com.finalproject.airport.approval.repository.ApprovalRepository;
import com.finalproject.airport.approval.service.ApprovalService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaggageClaimService {

    private final BaggageClaimRepository repository;
    private final ModelMapper modelMapper;
    private final AirplaneRepository airplaneRepository;
    private final ApprovalService approvalService;

    @Autowired
    public BaggageClaimService(BaggageClaimRepository repository ,ModelMapper modelMapper, AirplaneRepository airplaneRepository, ApprovalService approvalService){
        this.modelMapper = modelMapper;
        this.repository = repository;
        this.airplaneRepository = airplaneRepository;
        this.approvalService = approvalService;
    }

    @Autowired
    private ApprovalRepository approvalRepository;

    public List<BaggageClaimDTO> findAll() {
        List<BaggageClaim> baggageClaimList = repository.findByisActive("Y");

        return baggageClaimList.stream()
                .map(baggageClaim -> modelMapper.map(baggageClaim, BaggageClaimDTO.class))
                .collect(Collectors.toList());
    }

    public BaggageClaimDTO findBybaggageClaimCode(int baggageClaimCode) {
        BaggageClaim baggageClaim = repository.findBybaggageClaimCode(baggageClaimCode);
        return modelMapper.map(baggageClaim,BaggageClaimDTO.class);
    }

    @Transactional
    public String insertBaggageClaim(BaggageClaimDTO baggageClaim) {

        int result = 0;

        try {

        Airplane airplane = airplaneRepository.findByAirplaneCode(baggageClaim.getAirplaneCode());

        BaggageClaim insertBaggageClaim = BaggageClaim.builder()
                .airplane(airplane) // DTO에서 가져온 비행기 정보
                .lastInspectionDate(baggageClaim.getLastInspectionDate()) // 최근 점검 날짜
                .location(baggageClaim.getLocation()) // 위치
                .manager(baggageClaim.getManager()) // 담당자
                .note(baggageClaim.getNote()) // 비고
                .status(baggageClaim.getStatus()) // 상태
                .type(baggageClaim.getType()) // 타입
                .isActive("N") // 활성화/비활성화 필드 추가
                .build();

            BaggageClaim baggageClaim1 = repository.save(insertBaggageClaim);

            // 승인 정보 저장
            ApprovalDTO approvalDTO = new ApprovalDTO(
                    ApprovalTypeEntity.등록,
                    ApprovalStatusEntity.N,
                    null,
                    null,
                    baggageClaim1.getBaggageClaimCode(),
                    null,
                    null
            );

            approvalService.saveBaggageClaimApproval(approvalDTO);

            result = 1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return (result > 0) ? "수화물 수취대 승인 요청 성공" : "수화물 수취대 승인 요청 실패";

    }


    @Transactional
    public String modifyBaggageClaim(BaggageClaimDTO baggageClaim) {

        System.out.println("baggageClaim = " + baggageClaim);
        int result = 0;

        try {

            BaggageClaim modifybaggageClaim = BaggageClaim.builder()
                    .location(baggageClaim.getLocation())
                    .type(baggageClaim.getType())
                    .status(baggageClaim.getStatus())
                    .registrationDate(baggageClaim.getRegistrationDate())
                    .lastInspectionDate(baggageClaim.getLastInspectionDate())
                    .manager(baggageClaim.getManager())
                    .note(baggageClaim.getNote())
                    .isActive("N") // 활성화/비활성화 필드 추가
                    .build();

            BaggageClaim baggageClaim1 = repository.save(modifybaggageClaim);


            //2. 수정 후의 데이터 저장
            ApprovalEntity approval = new ApprovalEntity(
                    ApprovalTypeEntity.수정,
                    ApprovalStatusEntity.N,
                    null,
                    null,
                    baggageClaim1,
                    null,
                    null,
                    null,
                    baggageClaim.getBaggageClaimCode()
            );

            approvalRepository.save(approval);
            result = 1;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        return (result>0) ? "수화물 수취대 수정 승인 성공" : "수화물 수취대 수정 승인 요청 실패";
    }



//
//    @Transactional
//    public String modifybaggageClaim(int baggageClaimCode, BaggageClaimDTO baggageClaim) {
//
//        int result = 0;
//
//        try {
//            // 1. 기존 데이터 조회
//            BaggageClaim existingBaggageClaim = repository.findById(baggageClaimCode)
//                    .orElseThrow(() -> new RuntimeException("BaggageClaim not found"));
//
//            // 2. 기존 데이터를 승인 엔티티에 저장
//            ApprovalDTO approvalDTOBefore = new ApprovalDTO(
//                    ApprovalTypeEntity.수정,
//                    ApprovalStatusEntity.N,
//                    null,
//                    null,
//                    baggageClaimCode,
//                    null,
//                    null
//            );
//
//            // 기존 데이터를 승인 엔티티에 저장
//            ApprovalEntity approvalEntityBefore = new ApprovalEntity(
//                    approvalDTOBefore.getType(),
//                    approvalDTOBefore.getStatus(),
//                    null,
//                    null,
//                    existingBaggageClaim,
//                    null,
//                    null
//            );
//            approvalRepository.save(approvalEntityBefore);
//
//            // 3. 데이터 수정
//            existingBaggageClaim.setLocation(baggageClaim.getLocation());
//            existingBaggageClaim.setType(baggageClaim.getType());
//            existingBaggageClaim.setStatus(baggageClaim.getStatus());
//            existingBaggageClaim.setRegistrationDate(baggageClaim.getRegistrationDate());
//            existingBaggageClaim.setLastInspectionDate(baggageClaim.getLastInspectionDate());
//            existingBaggageClaim.setManager(baggageClaim.getManager());
//            existingBaggageClaim.setNote(baggageClaim.getNote());
//            existingBaggageClaim.setIsActive("N"); // 비활성 상태로 설정
//
//            BaggageClaim updatedBaggageClaim = repository.save(existingBaggageClaim);
//
//            // 4. 수정된 데이터로 승인 엔티티 저장
//            ApprovalDTO approvalDTOAfter = new ApprovalDTO(
//                    ApprovalTypeEntity.수정,
//                    ApprovalStatusEntity.N,
//                    null,
//                    null,
//                    updatedBaggageClaim.getBaggageClaimCode(),
//                    null,
//                    null
//            );
//
//            ApprovalEntity approvalEntityAfter = new ApprovalEntity(
//                    approvalDTOAfter.getType(),
//                    approvalDTOAfter.getStatus(),
//                    null,
//                    null,
//                    updatedBaggageClaim,
//                    null,
//                    null
//            );
//            approvalRepository.save(approvalEntityAfter);
//
//            result = 1;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return (result > 0) ? "수화물 수취대 수정 승인 성공" : "수화물 수취대 수정 승인 요청 실패";
//    }


    @Transactional
    public void softDelete(int baggageClaimCode) {
        BaggageClaim baggageClaim = repository.findBybaggageClaimCode(baggageClaimCode);
        baggageClaim = baggageClaim.toBuilder().isActive("N").build();

        repository.save(baggageClaim);

    }


}
