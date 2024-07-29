package com.finalproject.airport.airplane.checkincounter.service;

import com.finalproject.airport.airplane.airplane.Entity.Airplane;
import com.finalproject.airport.airplane.airplane.repository.AirplaneRepository;
import com.finalproject.airport.airplane.checkincounter.dto.CheckinCounterDTO;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounter;
import com.finalproject.airport.airplane.checkincounter.repository.CheckinCounterRepository;
import com.finalproject.airport.airplane.gate.dto.GateDTO;
import com.finalproject.airport.airplane.gate.entity.Gate;
import com.finalproject.airport.approval.dto.ApprovalDTO;
import com.finalproject.airport.approval.entity.ApprovalStatusEntity;
import com.finalproject.airport.approval.entity.ApprovalTypeEntity;
import com.finalproject.airport.approval.service.ApprovalService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CheckinCounterService {

    private final CheckinCounterRepository repository;
    private final AirplaneRepository airplaneRepository;
    private final ModelMapper modelMapper;
    private final ApprovalService approvalService;

    @Autowired
    public CheckinCounterService(CheckinCounterRepository repository, ModelMapper modelMapper,AirplaneRepository airplaneRepository, ApprovalService approvalService){
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.airplaneRepository = airplaneRepository;
        this.approvalService = approvalService;
    }

    @Transactional
    public String insertchkinCounter(CheckinCounterDTO chkinCounter) {

        int result = 0;

        try {

        Airplane airplane = airplaneRepository.findByAirplaneCode(chkinCounter.getAirplaneCode());

        CheckinCounter insertchkinCounter = CheckinCounter.builder()
                .airplane(airplane) // DTO에서 가져온 비행기 정보
                .lastInspectionDate(chkinCounter.getLastInspectionDate()) // 최근 점검 날짜
                .location(chkinCounter.getLocation()) // 위치
                .manager(chkinCounter.getManager()) // 담당자
                .note(chkinCounter.getNote()) // 비고
                .status(chkinCounter.getStatus()) // 상태
                .type(chkinCounter.getType()) // 타입
                .isActive("N") // 활성화/비활성화 필드 추가
                .build();

        CheckinCounter checkinCounter =  repository.save(insertchkinCounter);
            System.out.println("checkinCounter = " + checkinCounter);

            // 승인 정보 저장
            ApprovalDTO approvalDTO = new ApprovalDTO(
                    ApprovalTypeEntity.등록,
                    ApprovalStatusEntity.N,
                    null,
                    checkinCounter.getCheckinCounterCode(),
                    null,
                    null
            );

            approvalService.saveChkinCounterApproval(approvalDTO);

            result = 1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return (result > 0) ? "체크인 카운터 승인 요청 성공" : "체크인 카운터 승인 요청 실패";


    }

    public List<CheckinCounterDTO> findAll() {
        List<CheckinCounter> checkinCounters = repository.findByisActive("Y");

        System.out.println("checkinCounters = " + checkinCounters);
        return checkinCounters.stream()
                .map(chkinCounter -> modelMapper.map(chkinCounter, CheckinCounterDTO.class))
                .collect(Collectors.toList());
    }

    public CheckinCounterDTO findBycheckinCounterCode(int checkinCounterCode) {

        CheckinCounter chkinCounter = repository.findBycheckinCounterCode(checkinCounterCode);
        return modelMapper.map(chkinCounter,CheckinCounterDTO.class);
    }

    @Transactional
    public void modifyCheckinCounter(int checkinCounterCode, CheckinCounterDTO modifyCheckinCounter) {


        CheckinCounter checkinCounter = repository.findBycheckinCounterCode(checkinCounterCode);


        checkinCounter =  checkinCounter.toBuilder()
//                .location(modifyCheckinCounter.getLocation())
//                .type(modifyCheckinCounter.getType())
                .status(modifyCheckinCounter.getStatus())
                .registrationDate(modifyCheckinCounter.getRegistrationDate())
                .lastInspectionDate(modifyCheckinCounter.getLastInspectionDate())
                .manager(modifyCheckinCounter.getManager())
                .note(modifyCheckinCounter.getNote())
                .build();

        repository.save(checkinCounter);
    }

    @Transactional
    public void remodveCheckinCounter(int checkinCounterCode) {
        CheckinCounter checkinCounter = repository.findBycheckinCounterCode(checkinCounterCode);
        checkinCounter = checkinCounter.toBuilder().isActive("N").build();

        repository.save(checkinCounter);
    }
}
