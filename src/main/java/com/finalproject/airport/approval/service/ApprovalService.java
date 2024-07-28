package com.finalproject.airport.approval.service;

import com.finalproject.airport.airplane.gate.entity.Gate;
import com.finalproject.airport.airplane.gate.repository.GateRepository;
import com.finalproject.airport.approval.dto.ApprovalDTO;
import com.finalproject.airport.approval.entity.ApprovalEntity;
import com.finalproject.airport.approval.entity.ApprovalStatusEntity;
import com.finalproject.airport.approval.repository.ApprovalRepository;
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

    @Autowired
    public ApprovalService(ApprovalRepository repository, ModelMapper modelMapper, GateRepository gateRepository) {
        this.approvalRepository = repository;
        this.modelMapper = modelMapper;
        this.gateRepository = gateRepository;
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
                approvalDTO.getGatecode(),
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
                approvalDTO.getCheckincountercode(),
                null);

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
                approvalDTO.getBaggageclaimcode());

        System.out.println("approvalEntity: " + approvalEntity);
        approvalRepository.save(approvalEntity);
    }


    @Transactional
    public void approve(Integer approvalCode) {
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
        } else {
            throw new RuntimeException("Approval not found: " + approvalCode);
        }
    }


}
