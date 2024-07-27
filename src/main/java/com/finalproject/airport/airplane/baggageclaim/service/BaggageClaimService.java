package com.finalproject.airport.airplane.baggageclaim.service;


import com.finalproject.airport.airplane.airplane.Entity.Airplane;
import com.finalproject.airport.airplane.airplane.repository.AirplaneRepository;
import com.finalproject.airport.airplane.baggageclaim.dto.BaggageClaimDTO;
import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaim;
import com.finalproject.airport.airplane.baggageclaim.repository.BaggageClaimRepository;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounter;
import com.finalproject.airport.airplane.gate.dto.GateDTO;
import com.finalproject.airport.airplane.gate.entity.Gate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaggageClaimService {

    private final BaggageClaimRepository repository;
    private final ModelMapper modelMapper;
    private final AirplaneRepository airplaneRepository;

    @Autowired
    public BaggageClaimService(BaggageClaimRepository repository ,ModelMapper modelMapper, AirplaneRepository airplaneRepository){
        this.modelMapper = modelMapper;
        this.repository = repository;
        this.airplaneRepository = airplaneRepository;
    }

    public List<BaggageClaimDTO> findAll() {
        List<BaggageClaim> baggageClaimList = repository.findByisActive("Y");

        return baggageClaimList.stream()
                .map(baggageClaim -> modelMapper.map(baggageClaim, BaggageClaimDTO.class))
                .collect(Collectors.toList());
    }

    public BaggageClaimDTO findBybaggageClaimCode(int baggageClaimCode) {
        BaggageClaim baggageClaim = repository.findBybaggageClaimCode(baggageClaimCode);
        return modelMapper.map(baggageClaim,BaggageClaimDTO.class);
    }

    @Transactional
    public void insertBaggageClaim(BaggageClaimDTO baggageClaim) {

        Airplane airplane = airplaneRepository.findByAirplaneCode(baggageClaim.getAirplaneCode());

        BaggageClaim insertBaggageClaim = BaggageClaim.builder()
                .airplane(airplane) // DTO에서 가져온 비행기 정보
                .lastInspectionDate(baggageClaim.getLastInspectionDate()) // 최근 점검 날짜
                .location(baggageClaim.getLocation()) // 위치
                .manager(baggageClaim.getManager()) // 담당자
                .note(baggageClaim.getNote()) // 비고
                .status(baggageClaim.getStatus()) // 상태
                .type(baggageClaim.getType()) // 타입
                .isActive("Y") // 활성화/비활성화 필드 추가
                .build();

            repository.save(insertBaggageClaim);




    }


    @Transactional
    public void modifybaggageClaim(int baggageClaimCode, BaggageClaimDTO modifybaggageClaim) {


        BaggageClaim baggageClaim = repository.findBybaggageClaimCode(baggageClaimCode);


        baggageClaim =  baggageClaim.toBuilder()
                .location(modifybaggageClaim.getLocation())
                .type(modifybaggageClaim.getType())
                .status(modifybaggageClaim.getStatus())
                .registrationDate(modifybaggageClaim.getRegistrationDate())
                .lastInspectionDate(modifybaggageClaim.getLastInspectionDate())
                .manager(modifybaggageClaim.getManager())
                .note(modifybaggageClaim.getNote())
                .build();

        repository.save(baggageClaim);

    }

    @Transactional
    public void softDelete(int baggageClaimCode) {
        BaggageClaim baggageClaim = repository.findBybaggageClaimCode(baggageClaimCode);
        baggageClaim = baggageClaim.toBuilder().isActive("N").build();

        repository.save(baggageClaim);

    }
}
