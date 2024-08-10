package com.finalproject.airport.airplane.baggageclaim.service;


import com.finalproject.airport.airplane.airplane.Entity.ArrivalAirplane;
import com.finalproject.airport.airplane.airplane.Entity.DepartureAirplane;
import com.finalproject.airport.airplane.airplane.repository.ArrivalAirplaneRepository;
import com.finalproject.airport.airplane.airplane.repository.DepartureAirplaneRepository;
import com.finalproject.airport.airplane.baggageclaim.dto.BaggageClaimDTO;
import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaim;
import com.finalproject.airport.airplane.baggageclaim.repository.BaggageClaimRepository;
import com.finalproject.airport.approval.entity.ApprovalEntity;
import com.finalproject.airport.approval.entity.ApprovalTypeEntity;
import com.finalproject.airport.approval.repository.ApprovalRepository;
import com.finalproject.airport.approval.service.ApprovalService;
import com.finalproject.airport.manager.entity.ManagersEntity;
import com.finalproject.airport.manager.repository.ManagersRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BaggageClaimService {

    private final BaggageClaimRepository repository;
    private final ModelMapper modelMapper;
    private final DepartureAirplaneRepository departureAirplaneRepository;

    private final ArrivalAirplaneRepository airplaneRepository;
    private final ApprovalService approvalService;
    private final ManagersRepository managersRepository;


    @Autowired
    public BaggageClaimService(BaggageClaimRepository repository , ModelMapper modelMapper, DepartureAirplaneRepository departureAirplaneRepository, ArrivalAirplaneRepository airplaneRepository, ApprovalService approvalService, ManagersRepository managersRepository){
        this.modelMapper = modelMapper;
        this.repository = repository;
        this.departureAirplaneRepository = departureAirplaneRepository;
        this.airplaneRepository = airplaneRepository;
        this.approvalService = approvalService;
        this.managersRepository = managersRepository;
    }

    @Autowired
    private ApprovalRepository approvalRepository;

    public List<BaggageClaimDTO> findAll() {
        List<BaggageClaim> baggageClaimList = repository.findByisActive("Y");



        List<BaggageClaimDTO> list =  baggageClaimList.stream()
                .map(baggageClaim -> modelMapper.map(baggageClaim, BaggageClaimDTO.class))
                .collect(Collectors.toList());

        for (BaggageClaimDTO baggageClaimDTO : list) {
            List<ManagersEntity> managersEntityList = managersRepository.findAllByBaggageClaimCodeAndIsActive(baggageClaimDTO.getBaggageClaimCode(),"Y");
            List<String> managerNames = new ArrayList<>();
            for (ManagersEntity managersEntity : managersEntityList) {
                managerNames.add(managersEntity.getUser().getUserName());
            }
            baggageClaimDTO.setManager(String.join(",", managerNames));
        }

        return list;
    }

    public BaggageClaimDTO findBybaggageClaimCode(int baggageClaimCode) {
        BaggageClaim baggageClaim = repository.findBybaggageClaimCode(baggageClaimCode);
        return modelMapper.map(baggageClaim,BaggageClaimDTO.class);
    }

    @Transactional
    public String insertBaggageClaim(BaggageClaimDTO baggageClaim) {

        int result = 0;

        try {

        DepartureAirplane departureAirplane = airplaneRepository.findByAirplaneCode(baggageClaim.getAirplaneCode());

        BaggageClaim insertBaggageClaim = BaggageClaim.builder()
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
            ApprovalEntity approval = new ApprovalEntity(
                    ApprovalTypeEntity.등록,
                    "N",
                    null,
                    null,
                    baggageClaim1,
                    null,
                    null
            );

            approvalRepository.save(approval);

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

            BaggageClaim baggageClaim1 = repository.findBybaggageClaimCode(baggageClaim.getBaggageClaimCode());
            baggageClaim1 = baggageClaim1.toBuilder()
                    .location(baggageClaim.getLocation())
                    .type(baggageClaim.getType())
                    .status(baggageClaim.getStatus())
                    .registrationDate(baggageClaim.getRegistrationDate())
                    .lastInspectionDate(baggageClaim.getLastInspectionDate())
                    .manager(baggageClaim.getManager())
                    .note(baggageClaim.getNote())
                    .build();

            BaggageClaim baggage = repository.save(baggageClaim1);


            //2. 수정 후의 데이터 저장
            ApprovalEntity approval = new ApprovalEntity(
                    ApprovalTypeEntity.수정,
                    "N",
                    null,
                    null,
                    baggage,
                    null,
                    null,
                    null,
                    null
            );

            approvalRepository.save(approval);

            result = 1;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        return (result>0) ? "수화물 수취대 수정 승인 성공" : "수화물 수취대 수정 승인 요청 실패";
    }



    @Transactional
    public void softDelete(int baggageClaimCode) {
        BaggageClaim baggageClaim = repository.findBybaggageClaimCode(baggageClaimCode);
        baggageClaim = baggageClaim.toBuilder().isActive("N").build();

        repository.save(baggageClaim);

    }


    @Transactional
    public List<BaggageClaimDTO> baggageClaimfeach() {
        LocalDateTime now = LocalDateTime.now();
        Map<Integer, ArrivalAirplane> closestAirplanes = new HashMap<>();
        Map<Integer, LocalDateTime> closestTimes = new HashMap<>();

        // 수하물 찾는 곳 번호 범위 지정 (예: 1번부터 20번까지)
        for (int i = 1; i <= 13; i++) {
            // 해당 수하물 찾는 곳 번호와 연결된 도착 비행기 리스트 가져오기
            List<ArrivalAirplane> arrivalAirplaneList = airplaneRepository.findByCarousel(i);

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

        // 수하물 찾는 곳 상태 업데이트 및 출력
        closestAirplanes.forEach((baggageClaimNumber, closestAirplane) -> {
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

            System.out.println("수하물 찾는 곳 번호: " + baggageClaimNumber);
            System.out.println("가장 가까운 도착 비행기 시간: " + arrivalTime);
            System.out.println("항공사: " + closestAirplane.getAirline());
            System.out.println("상태: " + status);

            // 데이터베이스에서 baggageClaimNumber와 일치하는 BaggageClaim 객체 찾기
            Optional<BaggageClaim> optionalBaggageClaim = repository.findByBaggageClaimCode(baggageClaimNumber);

            BaggageClaim baggageClaim;
            if (optionalBaggageClaim.isPresent()) {
                baggageClaim = optionalBaggageClaim.get();
                baggageClaim.updateBaggageClaim(
                        Timestamp.valueOf(arrivalTime),
                        closestAirplane.getAirline(),
                        status
                );
            } else {
                baggageClaim = BaggageClaim.builder()

                        .baggageClaimCode(baggageClaimNumber)
                        .isActive(status) // 새로 생성되는 BaggageClaim 객체의 상태도 설정
                        .build();
            }

            repository.save(baggageClaim);
        });

        return null; // 실제로는 List<BaggageClaimDTO>를 반환해야 함
    }

    public void insertdb() {
        for (int i = 1 ; i <= 13 ; i++) {
            BaggageClaim baggageClaim = new BaggageClaim();
            baggageClaim.toBuilder()
                    .baggageClaimCode(i)
                    .isActive("Y");
            repository.save(baggageClaim);
        }
    }
}
