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

    private final DepartureAirplaneRepository departureAirplaneRepository;
    private final ApprovalRepository approvalRepository;

    public GateService(GateRepository gateRepository, ModelMapper modelMapper, ApprovalService approvalService , DepartureAirplaneRepository departureAirplaneRepository, ApprovalRepository approvalRepository){

        this.gateRepository = gateRepository;
        this.modelMapper = modelMapper;
        this.approvalService = approvalService;
        this.departureAirplaneRepository = departureAirplaneRepository;
        this.approvalRepository = approvalRepository;
    }


    public List<GateDTO> findAll() {


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
    public String modifyGate(GateDTO modifyGate) {

        System.out.println("modifyGate = " + modifyGate);
        int result = 0;

        try{
            Gate gate =  gateRepository.findAllBygateCode(modifyGate.getGateCode());
            gate = gate.toBuilder()
                    .manager(modifyGate.getManager())
                    .type(modifyGate.getGateType())
                    .note(modifyGate.getNote())
                    .location(modifyGate.getLocation())
                    .status(modifyGate.getStatus())
                    .lastInspectionDate(modifyGate.getLastInspectionDate())
                    .registrationDate(modifyGate.getRegistrationDate())
                    .build();
            System.out.println("gate = " + gate);
            Gate gate1 = gateRepository.save(gate);
            System.out.println("gate1 = " + gate1);

            ApprovalEntity approval = new ApprovalEntity(
                    ApprovalTypeEntity.수정,
                    "N",
                    gate1,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null

            );
            approvalRepository.save(approval);
            result = 1;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return (result>0) ? "게이트 승인 수정 성공" : "게이트 승인 수정 실패";
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

            DepartureAirplane departureAirplane = departureAirplaneRepository.findByAirplaneCode(gateDTO.getAirplaneCode());

            Gate insertGate = Gate.builder()
                    .departureAirplane(departureAirplane) // DTO에서 가져온 비행기 정보
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
            ApprovalEntity approval = new ApprovalEntity(
                    ApprovalTypeEntity.등록,
                    "N",
                    gate,
                    null,
                    null,
                    null,
                    null
            );

            approvalRepository.save(approval);

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
    @Transactional
    public List<GateDTO> feach() {
        LocalDateTime now = LocalDateTime.now();
        Map<Integer, DepartureAirplane> closestAirplanes = new HashMap<>();
        Map<Integer, LocalDateTime> closestTimes = new HashMap<>();

        for (int i = 6; i <= 132; i++) {
            List<DepartureAirplane> departureAirplaneList = departureAirplaneRepository.findByGatenumber(i); //게이트 다가져옴 넘버 기준으로

            for (DepartureAirplane departureAirplane : departureAirplaneList) {    // 게이트 뽑기
                LocalDateTime scheduleTime = departureAirplane.getScheduleDateTime().toLocalDateTime(); //시간 뽑는데 로컬데이트타임으로 뽑기

                if (scheduleTime.isAfter(now)) {
                    //뽑은 데이터 중 현재 시간기준으로 30분 이후 애들만 가져옴 그러면 이전 시간은 자동으로 거르기
                    if (!closestTimes.containsKey(i) || scheduleTime.isBefore(closestTimes.get(i))) {
                        closestAirplanes.put(i, departureAirplane);
                        closestTimes.put(i, scheduleTime);
                    }
                }
            }
        }

        closestAirplanes.forEach((gateNumber, closestAirplane) -> {
            LocalDateTime scheduleTime = closestAirplane.getScheduleDateTime().toLocalDateTime();
            LocalDateTime thirtyMinutesBeforeSchedule = scheduleTime.minusMinutes(30);

            String status;
            // 비행기 일정 시간 30분 전부터 비행기 일정 시간까지 "사용중"
            if (now.isAfter(thirtyMinutesBeforeSchedule) && now.isBefore(scheduleTime)) {
                status = "사용중";
            } else {
                // 그 외의 경우는 "사용가능"
                status = "사용가능";
            }

            System.out.println("게이트 번호: " + gateNumber);
            System.out.println("가장 가까운 비행기 시간: " + scheduleTime);
            System.out.println("항공사: " + closestAirplane.getAirline());
            System.out.println("상태: " + status);

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
                        .departureAirplane(closestAirplane)
                        .scheduleDateTime(Timestamp.valueOf(scheduleTime))
                        .gateCode(gateNumber)
                        .airline(closestAirplane.getAirline())
                        .isActive(status) // 새로 생성되는 Gate 객체의 상태도 설정
                        .build();
            }

            gateRepository.save(gate);
        });
        return null;
    }
    @Transactional
    public List<GateDTO> gateList1() {
        // GateCode가 101부터 132까지인 활성화된 게이트만 조회
        List<Gate> gateList = gateRepository.findByGateCodeBetween(101, 132);
        System.out.println(gateList);
        // Gate 엔티티를 GateDTO로 변환하여 반환
        return gateList.stream()
                .filter(gate -> "Y".equals(gate.getIsActive())) // 활성화된 게이트만 필터링
                .map(gate -> modelMapper.map(gate, GateDTO.class))
                .collect(Collectors.toList());
    }

    public List<GateDTO> gateList2() {
        List<Gate> gateList = gateRepository.findByGateCodeBetween(30, 41);
        System.out.println(gateList);

        return gateList.stream()
                .filter(gate -> "Y".equals(gate.getIsActive()))
                .map(gate -> modelMapper.map(gate, GateDTO.class))
                .collect(Collectors.toList());
    }

    public List<GateDTO> gateList3() {
        List<Gate> gateList = gateRepository.findByGateCodeBetween(12, 26);
        System.out.println(gateList);

        return gateList.stream()
                .filter(gate -> "Y".equals(gate.getIsActive()))
                .map(gate -> modelMapper.map(gate, GateDTO.class))
                .collect(Collectors.toList());
    }
}
