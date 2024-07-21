package com.finalproject.airport.airplane.airplane.service;

import com.finalproject.airport.airplane.airplane.DTO.AirplaneDTO;
import com.finalproject.airport.airplane.airplane.Entity.Airplane;
import com.finalproject.airport.airplane.airplane.repository.AirplaneRepository;
import com.finalproject.airport.airplane.baggageclaim.dto.BaggageClaimDTO;
import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaim;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AirPlaneService {

    private final AirplaneRepository airplaneRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AirPlaneService(AirplaneRepository airplaneRepository , ModelMapper modelMapper) {
        this.airplaneRepository = airplaneRepository;
        this.modelMapper = modelMapper;
    }


    public List<AirplaneDTO> findAll() {
        List<Airplane> AirplaneList = airplaneRepository.findByisActive("Y");

        return AirplaneList.stream()
                .map(airplane -> modelMapper.map(airplane, AirplaneDTO.class))
                .collect(Collectors.toList());
    }

    public AirplaneDTO findByairplaneCode(int airplaneCode) {

        Airplane airplane  = airplaneRepository.findByairplaneCode(airplaneCode);
        return modelMapper.map(airplane, AirplaneDTO.class);
    }
}
