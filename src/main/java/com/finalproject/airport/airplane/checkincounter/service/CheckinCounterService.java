package com.finalproject.airport.airplane.checkincounter.service;

import com.finalproject.airport.airplane.airplane.Entity.DepartureAirplane;
import com.finalproject.airport.airplane.airplane.repository.DepartureAirplaneRepository;
import com.finalproject.airport.airplane.checkincounter.dto.CheckinCounterDTO;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounter;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounterLocation;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounterType;
import com.finalproject.airport.airplane.checkincounter.repository.CheckinCounterRepository;
import com.finalproject.airport.approval.entity.ApprovalEntity;
import com.finalproject.airport.approval.entity.ApprovalTypeEntity;
import com.finalproject.airport.approval.repository.ApprovalRepository;
import com.finalproject.airport.approval.service.ApprovalService;
import com.finalproject.airport.member.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
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
    public String insertchkinCounter(CheckinCounterDTO chkinCounter) {

        UserEntity user = modelMapper.map(chkinCounter.getApprovalRequester(), UserEntity.class);
        log.info("Inserting check-in counter: {}", chkinCounter);
        int result = 0;

        try {

            DepartureAirplane departureAirplane = departureAirplaneRepository.findByAirplaneCode(chkinCounter.getAirplaneCode());

            CheckinCounter insertchkinCounter = CheckinCounter.builder()
//                .departureAirplane(departureAirplane) // DTO에서 가져온 비행기 정보
                    .lastInspectionDate(chkinCounter.getLastInspectionDate()) // 최근 점검 날짜
                    .location(chkinCounter.getLocation()) // 위치
                    .manager(chkinCounter.getManager()) // 담당자
                    .note(chkinCounter.getNote()) // 비고
                    .status(chkinCounter.getStatus()) // 상태
                    .type(chkinCounter.getType()) // 타입
                    .isActive("N") // 활성화/비활성화 필드 추가
                    .approvalRequester(user)
                    .build();

            CheckinCounter checkinCounter =  repository.save(insertchkinCounter);
            log.info("Saved check-in counter: {}", checkinCounter);

            // 승인 정보 저장
            ApprovalEntity approval = new ApprovalEntity(
                    ApprovalTypeEntity.등록,
                    "N",
                    null,
                    checkinCounter,
                    null,
                    null,
                    null,"N"
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

        UserEntity user = modelMapper.map(modifyCheckinCounterDTO.getApprovalRequester() , UserEntity.class);

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
                    .approvalRequester(user)
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
                    null,"N"
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

    public void insertdb() {

        for (int i = 1 ; i <= 13 ; i++) {
            CheckinCounter checkinCounter = new CheckinCounter();
            checkinCounter.toBuilder()
                    .checkinCounterCode(i)
                    .isActive("Y");
            repository.save(checkinCounter);
        }


    }
    @Transactional
    public List<CheckinCounterDTO> feachdate() {
        LocalDateTime now = LocalDateTime.now();
        Map<Integer, DepartureAirplane> closestAirplanes = new HashMap<>();
        Map<Integer, Long> closestDifferences = new HashMap<>();

        List<DepartureAirplane> filteredAirplanes = getFilteredAirplanes().stream()
                .filter(airplane -> airplane.getScheduleDateTime().toLocalDateTime().isAfter(now))  // 과거 항공기 제외
                .collect(Collectors.toList());

        for (int i = 1; i <= 14; i++) {
            String prefix = String.valueOf((char) ('A' + i - 1));

            List<DepartureAirplane> departureAirplaneList = filteredAirplanes.stream()
                    .filter(airplane -> airplane.getChkinrange().startsWith(prefix))
                    .collect(Collectors.toList());

            for (DepartureAirplane airplane : departureAirplaneList) {
                LocalDateTime departureTime = airplane.getScheduleDateTime().toLocalDateTime();
                long difference = Duration.between(now, departureTime).toMillis();

                if (!closestDifferences.containsKey(i) || difference < closestDifferences.get(i)) {
                    closestDifferences.put(i, difference);
                    closestAirplanes.put(i, airplane);
                }
            }
        }

        for (Map.Entry<Integer, DepartureAirplane> entry : closestAirplanes.entrySet()) {
            Integer checkinCounterCode = entry.getKey();
            DepartureAirplane airplane = entry.getValue();

            CheckinCounter checkinCounter = repository.findByCheckinCounterCode(checkinCounterCode);
            if (checkinCounter != null) {
                checkinCounter.setLocation(CheckinCounterLocation.values()[checkinCounterCode - 1]);
                checkinCounter.setType(CheckinCounterType.A);
                checkinCounter.setRemark(airplane.getRemark());
                checkinCounter.setFlightid(airplane.getFlightId());
                checkinCounter.setAirline(airplane.getAirline());
                checkinCounter.setAirport(airplane.getAirport());
                checkinCounter.setScheduleDateTime(airplane.getScheduleDateTime());
                checkinCounter.setAirline(airplane.getAirline());

                repository.save(checkinCounter);
            }
        }

        return null;
    }


    public List<DepartureAirplane> getFilteredAirplanes() {
        List<DepartureAirplane> departureAirplaneList = departureAirplaneRepository.findAll();

        return departureAirplaneList.stream()
                .filter(departureAirplane -> {
                    String chkinrange = departureAirplane.getChkinrange();
                    return chkinrange != null && !chkinrange.isEmpty() &&
                            chkinrange.matches("^[A-N].*"); // A ~ N으로 시작하는지 확인
                })
                .collect(Collectors.toList());
    }



}
