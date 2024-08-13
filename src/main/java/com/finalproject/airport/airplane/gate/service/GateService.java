package com.finalproject.airport.airplane.gate.service;

import com.finalproject.airport.airplane.airplane.Entity.DepartureAirplane;
import com.finalproject.airport.airplane.airplane.repository.DepartureAirplaneRepository;
import com.finalproject.airport.airplane.gate.dto.GateDTO;
import com.finalproject.airport.airplane.gate.entity.Gate;
import com.finalproject.airport.airplane.gate.repository.GateRepository;
import com.finalproject.airport.approval.entity.ApprovalEntity;
import com.finalproject.airport.approval.entity.ApprovalTypeEntity;
import com.finalproject.airport.approval.repository.ApprovalRepository;
import com.finalproject.airport.approval.service.ApprovalService;
import com.finalproject.airport.manager.entity.ManagersEntity;
import com.finalproject.airport.manager.repository.ManagersRepository;
import com.finalproject.airport.member.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GateService {

    private final GateRepository gateRepository;
    private final ModelMapper modelMapper;
    private final ApprovalService approvalService;
    private final DepartureAirplaneRepository departureAirplaneRepository;
    private final ApprovalRepository approvalRepository;
    private final ManagersRepository managersRepository;

    public List<GateDTO> findAll() {
        List<Gate> gateList = gateRepository.findByisActive("Y");

        List<GateDTO> gateDTOList = gateList.stream()
                .map(gate -> modelMapper.map(gate, GateDTO.class))
                .collect(Collectors.toList());

        for (GateDTO gateDTO : gateDTOList) {
            List<ManagersEntity> managersEntityList = managersRepository.findAllByGateCodeAndIsActive(gateDTO.getGateCode(), "Y");
            List<String> managerNames = managersEntityList.stream()
                    .map(managersEntity -> managersEntity.getUser().getUserName())
                    .collect(Collectors.toList());
            gateDTO.setManager(String.join(",", managerNames));
        }

        return gateDTOList;
    }

    public GateDTO findByGateCode(int gateCode) {
        Gate gate = gateRepository.findBygateCode(gateCode);
        return modelMapper.map(gate, GateDTO.class);
    }

    @Transactional
    public String modifyGate(GateDTO modifyGate) {
        log.info("Modifying gate: {}", modifyGate);
        int result = 0;

        UserEntity user = modelMapper.map(modifyGate.getApprovalRequester(),UserEntity.class);

        try {
            Gate gate = gateRepository.findBygateCode(modifyGate.getGateCode());
            gate = gate.toBuilder()
                    .manager(modifyGate.getManager())
                    .type(modifyGate.getGateType())
                    .note(modifyGate.getNote())
                    .location(modifyGate.getLocation())
                    .status(modifyGate.getStatus())
                    .lastInspectionDate(modifyGate.getLastInspectionDate())
                    .registrationDate(modifyGate.getRegistrationDate())
                    .approvalRequester(user)
                    .build();
            log.info("Updated gate: {}", gate);
            Gate updatedGate = gateRepository.save(gate);
            log.info("Saved gate: {}", updatedGate);

            ApprovalEntity approval = new ApprovalEntity(
                    ApprovalTypeEntity.수정,
                    "N",
                    updatedGate,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,"N"
            );
            approvalRepository.save(approval);
            result = 1;
        } catch (Exception e) {
            log.error("Error modifying gate: ", e);
            throw new RuntimeException(e);
        }

        return (result > 0) ? "게이트 승인 수정 성공" : "게이트 승인 수정 실패";
    }

    @Transactional
    public void softDelete(int gateCode) {
        log.info("Soft deleting gate with code: {}", gateCode);
        Gate gate = gateRepository.findBygateCode(gateCode);
        gate = gate.toBuilder().isActive("N").build();
        gateRepository.save(gate);
    }

    @Transactional
    public String insertGate(GateDTO gateDTO) {
        int result = 0;

        UserEntity user = modelMapper.map(gateDTO.getApprovalRequester(),UserEntity.class);

        try {
            DepartureAirplane departureAirplane = departureAirplaneRepository.findByAirplaneCode(gateDTO.getAirplaneCode());

            Gate insertGate = Gate.builder()
                    .departureAirplane(departureAirplane)
                    .lastInspectionDate(gateDTO.getLastInspectionDate())
                    .location(gateDTO.getLocation())
                    .manager(gateDTO.getManager())
                    .note(gateDTO.getNote())
                    .status(gateDTO.getStatus())
                    .type(gateDTO.getGateType())
//                    .airport(gateDTO.getAirport())
//                    .flightid(gateDTO.getFlightid())
                    .isActive("N")
                    .approvalRequester(user)
                    .build();

            Gate savedGate = gateRepository.save(insertGate);
            log.info("Saved gate: {}", savedGate);

            ApprovalEntity approval = new ApprovalEntity(
                    ApprovalTypeEntity.등록,
                    "N",
                    savedGate,
                    null,
                    null,
                    null,
                    null,"N"
            );
            approvalRepository.save(approval);

            result = 1;
        } catch (Exception e) {
            log.error("Error inserting gate: ", e);
            throw new RuntimeException(e);
        }

        return (result > 0) ? "탑승구 승인 요청 성공" : "탑승구 승인 요청 실패";
    }

    public List<Integer> findAllLocations() {
        List<Integer> locationList = gateRepository.findAlllocations();
        log.info("Location list: {}", locationList);

        return new ArrayList<>(locationList);
    }

    public void getTest() {
        for (int i = 6; i <= 132; i++) {
            Gate gate = Gate.builder()
                    .gateCode(i)
                    .build();
            gateRepository.save(gate);
        }
    }

    @Transactional
    public List<GateDTO> fetch() {
        LocalDateTime now = LocalDateTime.now();
        Map<Integer, DepartureAirplane> closestAirplanes = new HashMap<>();
        Map<Integer, LocalDateTime> closestTimes = new HashMap<>();

        for (int i = 6; i <= 132; i++) {
            List<DepartureAirplane> departureAirplaneList = departureAirplaneRepository.findByGatenumber(i);

            for (DepartureAirplane departureAirplane : departureAirplaneList) {
                LocalDateTime scheduleTime = departureAirplane.getScheduleDateTime().toLocalDateTime();

                if (scheduleTime.isAfter(now) && (!closestTimes.containsKey(i) || scheduleTime.isBefore(closestTimes.get(i)))) {
                    closestAirplanes.put(i, departureAirplane);
                    closestTimes.put(i, scheduleTime);
                }
            }
        }

        closestAirplanes.forEach((gateNumber, closestAirplane) -> {
            LocalDateTime scheduleTime = closestAirplane.getScheduleDateTime().toLocalDateTime();
            LocalDateTime thirtyMinutesBeforeSchedule = scheduleTime.minusMinutes(30);

            String status = (now.isAfter(thirtyMinutesBeforeSchedule) && now.isBefore(scheduleTime)) ? "사용중" : "사용가능";

            log.info("Gate number: {}", gateNumber);
            log.info("Closest airplane schedule time: {}", scheduleTime);
            log.info("Airline: {}", closestAirplane.getAirline());
            log.info("Status: {}", status);
            log.info("airport {}", closestAirplane.getAirport());

            Gate gate = gateRepository.findByGateCode(gateNumber)
                    .orElse(Gate.builder()
                            .departureAirplane(closestAirplane)
                            .scheduleDateTime(Timestamp.valueOf(scheduleTime))
                            .gateCode(gateNumber)
                            .airline(closestAirplane.getAirline())
                            .isActive(status)
                            .airport(closestAirplane.getAirport())
                            .flightid(closestAirplane.getFlightId())
                            .build());

            gate.updateGate(
                    Timestamp.valueOf(scheduleTime),
                    closestAirplane.getAirline(),
                    status,
                    closestAirplane.getAirport(),
                    closestAirplane.getFlightId()

            );

            gateRepository.save(gate);
        });

        return null;
    }

    @Transactional
    public List<GateDTO> gateList1() {
        List<Gate> gateList = gateRepository.findByGateCodeBetween(101, 132);
        log.info("Gate list 1: {}", gateList);
        return gateList.stream()
                .filter(gate -> "Y".equals(gate.getIsActive()))
                .map(gate -> modelMapper.map(gate, GateDTO.class))
                .collect(Collectors.toList());
    }

    public List<GateDTO> gateList2() {
        List<Gate> gateList = gateRepository.findByGateCodeBetween(30, 41);
        log.info("Gate list 2: {}", gateList);
        return gateList.stream()
                .filter(gate -> "Y".equals(gate.getIsActive()))
                .map(gate -> modelMapper.map(gate, GateDTO.class))
                .collect(Collectors.toList());
    }

    public List<GateDTO> gateList3() {
        List<Gate> gateList = gateRepository.findByGateCodeBetween(12, 26);
        log.info("Gate list 3: {}", gateList);
        return gateList.stream()
                .filter(gate -> "Y".equals(gate.getIsActive()))
                .map(gate -> modelMapper.map(gate, GateDTO.class))
                .collect(Collectors.toList());
    }
}
