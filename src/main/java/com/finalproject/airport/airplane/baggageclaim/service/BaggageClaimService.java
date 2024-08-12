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
import com.finalproject.airport.member.dto.ImgAndNameDTO;
import com.finalproject.airport.member.dto.UserDTO;
import com.finalproject.airport.member.entity.UserEntity;
import com.finalproject.airport.member.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BaggageClaimService {

    private final BaggageClaimRepository repository;
    private final ModelMapper modelMapper;
    private final DepartureAirplaneRepository departureAirplaneRepository;
    private final ArrivalAirplaneRepository airplaneRepository;
    private final ApprovalService approvalService;
    private final ManagersRepository managersRepository;
    private final ApprovalRepository approvalRepository;
    private final UserService userService;

    @Autowired
    public BaggageClaimService(BaggageClaimRepository repository, ModelMapper modelMapper,
                               DepartureAirplaneRepository departureAirplaneRepository,
                               ArrivalAirplaneRepository airplaneRepository,
                               ApprovalService approvalService, ManagersRepository managersRepository,
                               ApprovalRepository approvalRepository, UserService userService) {
        this.modelMapper = modelMapper;
        this.repository = repository;
        this.departureAirplaneRepository = departureAirplaneRepository;
        this.airplaneRepository = airplaneRepository;
        this.approvalService = approvalService;
        this.managersRepository = managersRepository;
        this.approvalRepository = approvalRepository;
        this.userService = userService;
    }

    public List<BaggageClaimDTO> findAll() {
        List<BaggageClaim> baggageClaimList = repository.findByisActive("Y");

        List<BaggageClaimDTO> list = baggageClaimList.stream()
                .map(baggageClaim -> modelMapper.map(baggageClaim, BaggageClaimDTO.class))
                .collect(Collectors.toList());

        for (BaggageClaimDTO baggageClaimDTO : list) {
            List<ManagersEntity> managersEntityList = managersRepository.findAllByBaggageClaimCodeAndIsActive(baggageClaimDTO.getBaggageClaimCode(), "Y");
            List<String> managerNames = managersEntityList.stream()
                    .map(managersEntity -> managersEntity.getUser().getUserName())
                    .collect(Collectors.toList());
            baggageClaimDTO.setManager(String.join(",", managerNames));
        }

        return list;
    }

    public BaggageClaimDTO findBybaggageClaimCode(int baggageClaimCode) {
        BaggageClaim baggageClaim = repository.findBybaggageClaimCode(baggageClaimCode);
        return modelMapper.map(baggageClaim, BaggageClaimDTO.class);
    }

    @Transactional
    public String insertBaggageClaim(BaggageClaimDTO baggageClaim) {
        int result = 0;

        UserEntity user = modelMapper.map(baggageClaim.getApprovalRequester() , UserEntity.class);
        try {
            DepartureAirplane departureAirplane = airplaneRepository.findByAirplaneCode(baggageClaim.getAirplaneCode());

            BaggageClaim insertBaggageClaim = BaggageClaim.builder()
                    .lastInspectionDate(baggageClaim.getLastInspectionDate())
                    .location(baggageClaim.getLocation())
                    .manager(baggageClaim.getManager())
                    .note(baggageClaim.getNote())
                    .status(baggageClaim.getStatus())
                    .type(baggageClaim.getType())
                    .isActive("N")
                    .approvalRequester(user)
                    .build();

            BaggageClaim baggageClaim1 = repository.save(insertBaggageClaim);

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
            log.error("Error inserting baggage claim: ", e);
            throw new RuntimeException(e);
        }

        return (result > 0) ? "수화물 수취대 승인 요청 성공" : "수화물 수취대 승인 요청 실패";
    }

    @Transactional
    public String modifyBaggageClaim(BaggageClaimDTO baggageClaim) {
        log.info("Modifying baggage claim: {}", baggageClaim);
        int result = 0;
        UserEntity user = modelMapper.map(baggageClaim.getApprovalRequester() , UserEntity.class);

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
                    .approvalRequester(user)
                    .build();

            BaggageClaim baggage = repository.save(baggageClaim1);

            System.out.println("baggage11111111111111 = " + baggage);
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
        } catch (Exception e) {
            log.error("Error modifying baggage claim: ", e);
            throw new RuntimeException(e);
        }

        return (result > 0) ? "수화물 수취대 수정 승인 성공" : "수화물 수취대 수정 승인 요청 실패";
    }


    @Transactional
    public void softDelete(int baggageClaimCode) {
        BaggageClaim baggageClaim = repository.findBybaggageClaimCode(baggageClaimCode);
        baggageClaim = baggageClaim.toBuilder().isActive("N").build();
        repository.save(baggageClaim);
    }

    @Transactional
    public List<BaggageClaimDTO> baggageClaimFetch() {
        LocalDateTime now = LocalDateTime.now();
        Map<Integer, ArrivalAirplane> closestAirplanes = new HashMap<>();
        Map<Integer, LocalDateTime> closestTimes = new HashMap<>();


        for (int i = 1; i <= 14; i++) {

            List<ArrivalAirplane> arrivalAirplaneList = airplaneRepository.findByCarousel(i);

            for (ArrivalAirplane arrivalAirplane : arrivalAirplaneList) {
                LocalDateTime arrivalTime = arrivalAirplane.getScheduleDateTime().toLocalDateTime();

                if (arrivalTime.isAfter(now)) {
                    if (!closestTimes.containsKey(i) || arrivalTime.isBefore(closestTimes.get(i))) {
                        closestAirplanes.put(i, arrivalAirplane);
                        closestTimes.put(i, arrivalTime);
                    }
                }
            }
        }

        closestAirplanes.forEach((baggageClaimNumber, closestAirplane) -> {
            LocalDateTime arrivalTime = closestAirplane.getScheduleDateTime().toLocalDateTime();
            LocalDateTime thirtyMinutesBeforeArrival = arrivalTime.minusMinutes(30);

            String status = (now.isAfter(thirtyMinutesBeforeArrival) && now.isBefore(arrivalTime)) ? "사용중" : "사용가능";

            log.info("수하물 찾는 곳 번호: {}", baggageClaimNumber);
            log.info("가장 가까운 도착 비행기 시간: {}", arrivalTime);
            log.info("항공사: {}", closestAirplane.getAirline());
            log.info("상태: {}", status);

            Optional<BaggageClaim> optionalBaggageClaim = repository.findByBaggageClaimCode(baggageClaimNumber);

            BaggageClaim baggageClaim;
            if (optionalBaggageClaim.isPresent()) {
                baggageClaim = optionalBaggageClaim.get();
                baggageClaim.updateBaggageClaim(
                        Timestamp.valueOf(arrivalTime),
                        closestAirplane.getAirline(),
                        status,
                        closestAirplane.getAirport(),
                        closestAirplane.getFlightId()
                );
            } else {
                baggageClaim = BaggageClaim.builder()
                        .baggageClaimCode(baggageClaimNumber)
                        .isActive(status)
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
