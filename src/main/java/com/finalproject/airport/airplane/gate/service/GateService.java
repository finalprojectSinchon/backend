package com.finalproject.airport.airplane.gate.service;

import com.finalproject.airport.airplane.airplane.Entity.Airplane;
import com.finalproject.airport.airplane.airplane.repository.AirplaneRepository;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounter;
import com.finalproject.airport.airplane.gate.dto.GateDTO;
import com.finalproject.airport.airplane.gate.entity.Gate;
import com.finalproject.airport.airplane.gate.repository.GateRepository;
import com.finalproject.airport.approval.dto.ApprovalDTO;
import com.finalproject.airport.approval.entity.ApprovalEntity;
import com.finalproject.airport.approval.entity.ApprovalStatusEntity;
import com.finalproject.airport.approval.entity.ApprovalTypeEntity;
import com.finalproject.airport.approval.service.ApprovalService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GateService {

    private final GateRepository gateRepository;

    private final ModelMapper modelMapper;

    private final ApprovalService approvalService;

    private final AirplaneRepository airplaneRepository;

    public GateService(GateRepository gateRepository, ModelMapper modelMapper, ApprovalService approvalService , AirplaneRepository airplaneRepository){

        this.gateRepository = gateRepository;
        this.modelMapper = modelMapper;
        this.approvalService = approvalService;
        this.airplaneRepository = airplaneRepository;
    }


    public List<GateDTO> findAll() {

        List<Gate> gateList = gateRepository.findByisActive("Y");

        return gateList.stream()
                .map(gate -> modelMapper.map(gate,GateDTO.class))
                .collect(Collectors.toList());
    }

    public GateDTO findBygateCode(int gateCode) {

        Gate gate = gateRepository.findBygateCode(gateCode);
        return modelMapper.map(gate,GateDTO.class);
    }

    @Transactional
    public void modifyGate(int gateCode, GateDTO modifyGate) {

        Gate gate = gateRepository.findBygateCode(gateCode);


        gate =  gate.toBuilder()
                .manager(modifyGate.getManager())
                .type(modifyGate.getGateType())
                .note(modifyGate.getNote())
                .location(modifyGate.getLocation())
                .status(modifyGate.getStatus())
                .lastInspectionDate(modifyGate.getLastInspectionDate())
                .registrationDate(modifyGate.getRegistrationDate())
                .build();

        gateRepository.save(gate);

    }

    @Transactional
    public void softDelete(int gateCode) {

       Gate gate = gateRepository.findBygateCode(gateCode);

        gate = gate.toBuilder().isActive("N").build();


       gateRepository.save(gate);

    }
    @Transactional
    public String insertGate(GateDTO gateDTO) {

        int result = 0;

        try {

            Airplane airplane = airplaneRepository.findByAirplaneCode(gateDTO.getAirplaneCode());

            Gate insertGate = Gate.builder()
                    .airplane(airplane) // DTO에서 가져온 비행기 정보
                    .lastInspectionDate(gateDTO.getLastInspectionDate()) // 최근 점검 날짜
                    .location(gateDTO.getLocation()) // 위치
                    .manager(gateDTO.getManager()) // 담당자
                    .note(gateDTO.getNote()) // 비고
                    .status(gateDTO.getStatus()) // 상태
                    .type(gateDTO.getGateType()) // 타입
                    .isActive("N") // 활성화/비활성화 필드 추가
                    .build();

            Gate gate = gateRepository.save(insertGate);
            System.out.println("gate = " + gate);

            // 승인 정보 저장
            ApprovalDTO approvalDTO = new ApprovalDTO(
                    ApprovalTypeEntity.등록,
                    ApprovalStatusEntity.N,
                    gate.getGateCode(),
                    null,
                    null
            );
            System.out.println("approvalDTO = " + approvalDTO);
            approvalService.saveGateApproval(approvalDTO);

            result = 1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return (result > 0) ? "탑승구 승인 요청 성공" : "탑승구 승인 요청 실패";

    }


}
