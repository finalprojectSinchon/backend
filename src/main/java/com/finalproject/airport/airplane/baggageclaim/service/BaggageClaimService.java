package com.finalproject.airport.airplane.baggageclaim.service;


import com.finalproject.airport.airplane.baggageclaim.dto.BaggageClaimDTO;
import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaim;
import com.finalproject.airport.airplane.baggageclaim.repository.BaggageClaimRepository;
import com.finalproject.airport.airplane.gate.dto.GateDTO;
import com.finalproject.airport.airplane.gate.entity.Gate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
