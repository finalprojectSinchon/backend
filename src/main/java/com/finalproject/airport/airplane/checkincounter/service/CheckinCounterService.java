package com.finalproject.airport.airplane.checkincounter.service;

import com.finalproject.airport.airplane.airplane.DTO.AirplaneDTO;
import com.finalproject.airport.airplane.airplane.Entity.ArrivalAirplane;
import com.finalproject.airport.airplane.airplane.Entity.DepartureAirplane;
import com.finalproject.airport.airplane.airplane.repository.DepartureAirplaneRepository;
import com.finalproject.airport.airplane.checkincounter.dto.CheckinCounterDTO;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounter;
import com.finalproject.airport.airplane.checkincounter.repository.CheckinCounterRepository;
import com.finalproject.airport.approval.entity.ApprovalEntity;
import com.finalproject.airport.approval.entity.ApprovalTypeEntity;
import com.finalproject.airport.approval.repository.ApprovalRepository;
import com.finalproject.airport.approval.service.ApprovalService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CheckinCounterService {

    private final CheckinCounterRepository repository;
    private final DepartureAirplaneRepository departureAirplaneRepository;
    private final ModelMapper modelMapper;
    private final ApprovalService approvalService;
    private final ApprovalRepository approvalRepository;

    @Autowired
    public CheckinCounterService(CheckinCounterRepository repository, ModelMapper modelMapper, DepartureAirplaneRepository departureAirplaneRepository, ApprovalService approvalService, ApprovalRepository approvalRepository){
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.departureAirplaneRepository = departureAirplaneRepository;
        this.approvalService = approvalService;
        this.approvalRepository = approvalRepository;
    }

    @Transactional
    public String insertchkinCounter(CheckinCounterDTO chkinCounter) {

        System.out.println("체킁인카운터서비스 = " + chkinCounter);
        int result = 0;

        try {

        DepartureAirplane departureAirplane = departureAirplaneRepository.findByAirplaneCode(chkinCounter.getAirplaneCode());

        CheckinCounter insertchkinCounter = CheckinCounter.builder()
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
            ApprovalEntity approval = new ApprovalEntity(
                    ApprovalTypeEntity.등록,
                    "N",
                    null,
                    checkinCounter,
                    null,
                    null,
                    null
            );

            approvalRepository.save(approval);

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
    public String modifyCheckinCounter(CheckinCounterDTO modifyCheckinCounter) {

        int result = 0;

        try {
            CheckinCounter checkinCounter =  repository.findBycheckinCounterCode(modifyCheckinCounter.getCheckinCounterCode());
                    checkinCounter = checkinCounter.toBuilder()
                    .location(modifyCheckinCounter.getLocation())
                    .type(modifyCheckinCounter.getType())
                    .status(modifyCheckinCounter.getStatus())
                    .registrationDate(modifyCheckinCounter.getRegistrationDate())
                    .lastInspectionDate(modifyCheckinCounter.getLastInspectionDate())
                    .manager(modifyCheckinCounter.getManager())
                    .note(modifyCheckinCounter.getNote())
                    .build();

            CheckinCounter checkinCounter1 = repository.save(checkinCounter);

            ApprovalEntity approval = new ApprovalEntity(
                    ApprovalTypeEntity.수정,
                    "N",
                    null,
                    checkinCounter1,
                    null,
                    null,
                    null,
                    null,
                    null

            );
            approvalRepository.save(approval);
            result = 1;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
     return (result > 0) ? "체크인 카운터 수정 승인 성공" : "체크인 카운터 수정 승인 실패";
    }


    @Transactional
    public void remodveCheckinCounter(int checkinCounterCode) {
        CheckinCounter checkinCounter = repository.findBycheckinCounterCode(checkinCounterCode);
        checkinCounter = checkinCounter.toBuilder().isActive("N").build();

        repository.save(checkinCounter);
    }

    @Transactional
    public List<CheckinCounterDTO> chkinCounterfeach() {
        LocalDateTime now = LocalDateTime.now();
        Map<Integer, ArrivalAirplane> closestAirplanes = new HashMap<>();
        Map<Integer, LocalDateTime> closestTimes = new HashMap<>();

        // 체크인 카운터 번호 범위 지정 (예: 1번부터 13번까지)
        for (int i = 1; i <= 13; i++) {
            // 해당 체크인 카운터 번호와 연결된 도착 비행기 리스트 가져오기
            List<ArrivalAirplane> arrivalAirplaneList = departureAirplaneRepository.findByCheckinCounter(i);

            for (ArrivalAirplane arrivalAirplane : arrivalAirplaneList) {
                LocalDateTime arrivalTime = arrivalAirplane.getScheduleDateTime().toLocalDateTime();

                // 현재 시간 이후의 도착 비행기만 고려
                if (arrivalTime.isAfter(now)) {
                    // 가장 가까운 도착 시간 업데이트
                    if (!closestTimes.containsKey(i) || arrivalTime.isBefore(closestTimes.get(i))) {
                        closestAirplanes.put(i, arrivalAirplane);
                        closestTimes.put(i, arrivalTime);
                    }
                }
            }
        }

        // 체크인 카운터 상태 업데이트 및 출력
        closestAirplanes.forEach((checkinCounterNumber, closestAirplane) -> {
            LocalDateTime arrivalTime = closestAirplane.getScheduleDateTime().toLocalDateTime();
            LocalDateTime thirtyMinutesBeforeArrival = arrivalTime.minusMinutes(30);

            String status;
            // 도착 시간 30분 전부터 도착 시간까지 "사용중"
            if (now.isAfter(thirtyMinutesBeforeArrival) && now.isBefore(arrivalTime)) {
                status = "사용중";
            } else {
                // 그 외의 경우는 "사용가능"
                status = "사용가능";
            }

            System.out.println("체크인 카운터 번호: " + checkinCounterNumber);
            System.out.println("가장 가까운 도착 비행기 시간: " + arrivalTime);
            System.out.println("항공사: " + closestAirplane.getAirline());
            System.out.println("상태: " + status);

            // 데이터베이스에서 checkinCounterNumber와 일치하는 CheckinCounter 객체 찾기
            Optional<CheckinCounter> optionalCheckinCounter = repository.findByCheckinCounterCode(checkinCounterNumber);

            CheckinCounter checkinCounter;
            if (optionalCheckinCounter.isPresent()) {
                checkinCounter = optionalCheckinCounter.get();
                checkinCounter.updateCheckinCounter(
                        Timestamp.valueOf(arrivalTime),
                        closestAirplane.getAirline(),
                        status
                );
            } else {
                checkinCounter = CheckinCounter.builder()
                        .checkinCounterCode(checkinCounterNumber)
                        .status(status) // 새로 생성되는 CheckinCounter 객체의 상태도 설정
                        .airline(closestAirplane.getAirline())
                        .build();
            }

            repository.save(checkinCounter);
        });

        // CheckinCounterDTO 리스트로 변환 후 반환
        return repository.findAll().stream()
                .map(entity -> new CheckinCounterDTO(
//                        entity.getCheckinCounterCode(),
//                        entity.getLocation(),
//                        entity.getType(),
//                        entity.getStatus(),
//                        entity.getRegistrationDate(),
//                        entity.getLastInspectionDate(),
//                        entity.getManager(),
//                        entity.getNote()
//                       여기부터 AR 말고 DE 로 전부 바꿔야함
                ))
                .collect(Collectors.toList());
    }

}
