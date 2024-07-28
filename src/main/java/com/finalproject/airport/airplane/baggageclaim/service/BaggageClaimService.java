package com.finalproject.airport.airplane.baggageclaim.service;


import com.finalproject.airport.airplane.airplane.Entity.Airplane;
import com.finalproject.airport.airplane.airplane.repository.AirplaneRepository;
import com.finalproject.airport.airplane.baggageclaim.dto.BaggageClaimDTO;
import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaim;
import com.finalproject.airport.airplane.baggageclaim.repository.BaggageClaimRepository;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounter;
import com.finalproject.airport.airplane.gate.dto.GateDTO;
import com.finalproject.airport.airplane.gate.entity.Gate;
import com.finalproject.airport.approval.dto.ApprovalDTO;
import com.finalproject.airport.approval.entity.ApprovalStatusEntity;
import com.finalproject.airport.approval.entity.ApprovalTypeEntity;
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
                    baggageClaim1.getBaggageClaimCode()
            );

            approvalService.saveBaggageClaimApproval(approvalDTO);

            result = 1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return (result > 0) ? "수화물 수취대 승인 요청 성공" : "수화물 수취대 승인 요청 실패";




    }


    @Transactional
    public void modifybaggageClaim(int baggageClaimCode, BaggageClaimDTO modifybaggageClaim) {


        BaggageClaim baggageClaim = repository.findBybaggageClaimCode(baggageClaimCode);


        baggageClaim =  baggageClaim.toBuilder()
                .location(modifybaggageClaim.getLocation())
                .type(modifybaggageClaim.getType())
                .status(modifybaggageClaim.getStatus())
                .registrationDate(modifybaggageClaim.getRegistrationDate())
                .lastInspectionDate(modifybaggageClaim.getLastInspectionDate())
                .manager(modifybaggageClaim.getManager())
                .note(modifybaggageClaim.getNote())
                .build();

        repository.save(baggageClaim);

    }

    @Transactional
    public void softDelete(int baggageClaimCode) {
        BaggageClaim baggageClaim = repository.findBybaggageClaimCode(baggageClaimCode);
        baggageClaim = baggageClaim.toBuilder().isActive("N").build();

        repository.save(baggageClaim);

    }
}
