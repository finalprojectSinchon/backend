package com.finalproject.airport.airplane.checkincounter.service;

import com.finalproject.airport.airplane.airplane.Entity.DepartureAirplane;
import com.finalproject.airport.airplane.airplane.repository.DepartureAirplaneRepository;
import com.finalproject.airport.airplane.checkincounter.dto.CheckinCounterDTO;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounter;
import com.finalproject.airport.airplane.checkincounter.repository.CheckinCounterRepository;
import com.finalproject.airport.approval.entity.ApprovalEntity;
import com.finalproject.airport.approval.entity.ApprovalTypeEntity;
import com.finalproject.airport.approval.repository.ApprovalRepository;
import com.finalproject.airport.approval.service.ApprovalService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CheckinCounterService {

    private final CheckinCounterRepository repository;
    private final DepartureAirplaneRepository departureAirplaneRepository;
    private final ModelMapper modelMapper;
    private final ApprovalService approvalService;
    private final ApprovalRepository approvalRepository;

    @Autowired
    public CheckinCounterService(CheckinCounterRepository repository, ModelMapper modelMapper,
                                 DepartureAirplaneRepository departureAirplaneRepository,
                                 ApprovalService approvalService, ApprovalRepository approvalRepository) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.departureAirplaneRepository = departureAirplaneRepository;
        this.approvalService = approvalService;
        this.approvalRepository = approvalRepository;
    }

    @Transactional
    public String insertCheckinCounter(CheckinCounterDTO checkinCounterDTO) {
        log.info("Inserting check-in counter: {}", checkinCounterDTO);
        int result = 0;

        try {
            DepartureAirplane departureAirplane = departureAirplaneRepository.findByAirplaneCode(checkinCounterDTO.getAirplaneCode());

            CheckinCounter checkinCounter = CheckinCounter.builder()
                    .departureAirplane(departureAirplane)
                    .lastInspectionDate(checkinCounterDTO.getLastInspectionDate())
                    .location(checkinCounterDTO.getLocation())
                    .manager(checkinCounterDTO.getManager())
                    .note(checkinCounterDTO.getNote())
                    .status(checkinCounterDTO.getStatus())
                    .type(checkinCounterDTO.getType())
                    .isActive("N")
                    .build();

            CheckinCounter savedCheckinCounter = repository.save(checkinCounter);
            log.info("Saved check-in counter: {}", savedCheckinCounter);

            ApprovalEntity approval = new ApprovalEntity(
                    ApprovalTypeEntity.등록,
                    "N",
                    null,
                    savedCheckinCounter,
                    null,
                    null,
                    null
            );

            approvalRepository.save(approval);
            result = 1;
        } catch (Exception e) {
            log.error("Error inserting check-in counter: ", e);
            throw new RuntimeException(e);
        }

        return (result > 0) ? "체크인 카운터 승인 요청 성공" : "체크인 카운터 승인 요청 실패";
    }

    public List<CheckinCounterDTO> findAll() {
        List<CheckinCounter> checkinCounters = repository.findByisActive("Y");
        log.info("Found check-in counters: {}", checkinCounters);
        return checkinCounters.stream()
                .map(checkinCounter -> modelMapper.map(checkinCounter, CheckinCounterDTO.class))
                .collect(Collectors.toList());
    }

    public CheckinCounterDTO findByCheckinCounterCode(int checkinCounterCode) {
        CheckinCounter checkinCounter = repository.findBycheckinCounterCode(checkinCounterCode);
        return modelMapper.map(checkinCounter, CheckinCounterDTO.class);
    }

    @Transactional
    public String modifyCheckinCounter(CheckinCounterDTO modifyCheckinCounterDTO) {
        log.info("Modifying check-in counter: {}", modifyCheckinCounterDTO);
        int result = 0;

        try {
            CheckinCounter checkinCounter = repository.findBycheckinCounterCode(modifyCheckinCounterDTO.getCheckinCounterCode());
            checkinCounter = checkinCounter.toBuilder()
                    .location(modifyCheckinCounterDTO.getLocation())
                    .type(modifyCheckinCounterDTO.getType())
                    .status(modifyCheckinCounterDTO.getStatus())
                    .registrationDate(modifyCheckinCounterDTO.getRegistrationDate())
                    .lastInspectionDate(modifyCheckinCounterDTO.getLastInspectionDate())
                    .manager(modifyCheckinCounterDTO.getManager())
                    .note(modifyCheckinCounterDTO.getNote())
                    .build();

            CheckinCounter updatedCheckinCounter = repository.save(checkinCounter);

            ApprovalEntity approval = new ApprovalEntity(
                    ApprovalTypeEntity.수정,
                    "N",
                    null,
                    updatedCheckinCounter,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            approvalRepository.save(approval);
            result = 1;
        } catch (Exception e) {
            log.error("Error modifying check-in counter: ", e);
            throw new RuntimeException(e);
        }

        return (result > 0) ? "체크인 카운터 수정 승인 성공" : "체크인 카운터 수정 승인 실패";
    }

    @Transactional
    public void removeCheckinCounter(int checkinCounterCode) {
        log.info("Removing check-in counter with code: {}", checkinCounterCode);
        CheckinCounter checkinCounter = repository.findBycheckinCounterCode(checkinCounterCode);
        checkinCounter = checkinCounter.toBuilder().isActive("N").build();
        repository.save(checkinCounter);
    }
}
