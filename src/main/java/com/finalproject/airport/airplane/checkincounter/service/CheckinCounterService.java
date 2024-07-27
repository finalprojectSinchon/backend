package com.finalproject.airport.airplane.checkincounter.service;

import com.finalproject.airport.airplane.airplane.Entity.Airplane;
import com.finalproject.airport.airplane.airplane.repository.AirplaneRepository;
import com.finalproject.airport.airplane.checkincounter.dto.CheckinCounterDTO;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounter;
import com.finalproject.airport.airplane.checkincounter.repository.CheckinCounterRepository;
import com.finalproject.airport.airplane.gate.dto.GateDTO;
import com.finalproject.airport.airplane.gate.entity.Gate;
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

    @Autowired
    public CheckinCounterService(CheckinCounterRepository repository, ModelMapper modelMapper,AirplaneRepository airplaneRepository){
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.airplaneRepository = airplaneRepository;
    }

    @Transactional
    public void insertchkinCounter(CheckinCounterDTO chkinCounter) {

        Airplane airplane = airplaneRepository.findByAirplaneCode(chkinCounter.getAirplaneCode());

        CheckinCounter insertchkinCounter = CheckinCounter.builder()
                .airplane(airplane) // DTO에서 가져온 비행기 정보
                .lastInspectionDate(chkinCounter.getLastInspectionDate()) // 최근 점검 날짜
                .location(chkinCounter.getLocation()) // 위치
                .manager(chkinCounter.getManager()) // 담당자
                .note(chkinCounter.getNote()) // 비고
                .status(chkinCounter.getStatus()) // 상태
                .type(chkinCounter.getType()) // 타입
                .isActive("Y") // 활성화/비활성화 필드 추가
                .build();

        repository.save(insertchkinCounter);
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
