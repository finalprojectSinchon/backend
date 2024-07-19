package com.finalproject.airport.airplane.baggageclaim.service;


import com.finalproject.airport.airplane.baggageclaim.dto.BaggageClaimDTO;
import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaim;
import com.finalproject.airport.airplane.baggageclaim.repository.BaggageClaimRepository;
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

    @Autowired
    public BaggageClaimService(BaggageClaimRepository repository ,ModelMapper modelMapper){
        this.modelMapper = modelMapper;
        this.repository = repository;
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
    public String insertBaggageClaim(BaggageClaimDTO baggageClaim) {

        int result = 0;
        try{

            BaggageClaim insertBaggageClaim = modelMapper.map(baggageClaim, BaggageClaim.class);
            repository.save(insertBaggageClaim);
            result = 1;
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        return (result > 0)? "수화물 수취대 등록 성공": "수화물 수취대 등록 실패";

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
