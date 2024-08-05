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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
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

    @Transactional
    public List<GateDTO> findAll() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thirtyMinutesBeforeNow = now.minusMinutes(30);

        // 게이트마다 가장 가까운 비행기를 저장할 맵
        Map<Integer, Airplane> closestAirplanes = new HashMap<>();
        Map<Integer, LocalDateTime> closestTimes = new HashMap<>();

        for (int i = 6; i <= 132; i++) {
            List<Airplane> airplaneList = airplaneRepository.findByGatenumber(i);

            for (Airplane airplane : airplaneList) {
                LocalDateTime scheduleTime = airplane.getScheduleDateTime().toLocalDateTime();

                if (scheduleTime.isAfter(thirtyMinutesBeforeNow)) {
                    if (!closestTimes.containsKey(i) || scheduleTime.isBefore(closestTimes.get(i))) {
                        closestAirplanes.put(i, airplane);
                        closestTimes.put(i, scheduleTime);
                    }
                }
            }
        }

        closestAirplanes.forEach((gateNumber, closestAirplane) -> {
            LocalDateTime scheduleTime = closestAirplane.getScheduleDateTime().toLocalDateTime();

            String status;
            if (scheduleTime.isAfter(now)) {
                // 비행기 일정 시간이 현재 시간보다 이후이면 '사용가능'
                status = "사용가능";
            } else if (scheduleTime.isAfter(thirtyMinutesBeforeNow)) {
                // 비행기 일정 시간이 현재 시간으로부터 30분 전 이후이면 '사용중'
                status = "사용중";
            } else {
                // 비행기 일정 시간이 현재 시간으로부터 30분 전보다 이전이면 '사용가능'
                status = "사용가능";
            }

            System.out.println("게이트 번호: " + gateNumber);
            System.out.println("가장 가까운 비행기 시간: " + scheduleTime);
            System.out.println("항공사: " + closestAirplane.getAirline());

            // 데이터베이스에서 gateNumber와 일치하는 Gate 객체 찾기
            Optional<Gate> optionalGate = gateRepository.findByGateCode(gateNumber);

            Gate gate;
            if (optionalGate.isPresent()) {
                gate = optionalGate.get();
                gate.updateGate(
                        Timestamp.valueOf(scheduleTime),
                        closestAirplane.getAirline(),
                        status
                );
            } else {
                gate = Gate.builder()
                        .airplane(closestAirplane)
                        .scheduleDateTime(Timestamp.valueOf(scheduleTime))
                        .gateCode(gateNumber)
                        .airline(closestAirplane.getAirline())
                        .isActive(status) // 새로 생성되는 Gate 객체의 상태도 설정
                        .build();
            }

            gateRepository.save(gate);
        });

        List<Gate> gateList = gateRepository.findByisActive("Y");

        return gateList.stream()
                .map(gate -> modelMapper.map(gate, GateDTO.class))
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
                    null,
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


    public List<Integer> findAlllocations() {

        List<Integer> locationList = gateRepository.findAlllocations();
        System.out.println("locationList = " + locationList);

        List<Integer> locations = new ArrayList<>();

        for(Integer gate : locationList){
            locations.add(gate);
        }

        System.out.println("locations = " + locations);
        return locations;
    }

    public void getTest() {
        for(int i = 6; i <= 132; i++){
            Gate gate = new Gate();
            gate = gate.toBuilder()
                    .gateCode(i)
                    .build();
            gateRepository.save(gate);
        }

    }
}
