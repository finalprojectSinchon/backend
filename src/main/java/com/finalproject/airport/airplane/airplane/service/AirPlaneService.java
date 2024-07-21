package com.finalproject.airport.airplane.airplane.service;

import com.finalproject.airport.airplane.airplane.DTO.AirplaneDTO;
import com.finalproject.airport.airplane.airplane.Entity.Airplane;
import com.finalproject.airport.airplane.airplane.repository.AirplaneRepository;
import com.finalproject.airport.airplane.baggageclaim.dto.BaggageClaimDTO;
import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaim;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
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

    @Transactional
    public void modifybAirplane(int airplaneCode, AirplaneDTO modifyairplane) {

        Airplane airplane = airplaneRepository.findByairplaneCode(airplaneCode);

        airplane =  airplane.toBuilder()
                .airline(modifyairplane.getAirline())
                .scheduleDateTime(modifyairplane.getScheduleDateTime())
                .remark(modifyairplane.getRemark())
                .airport(modifyairplane.getAirport())
                .flightId(modifyairplane.getFlightId())
                .carousel(modifyairplane.getCarousel())
                .gatenumber(modifyairplane.getGatenumber())
                .terminalid(modifyairplane.getTerminalid())
                .chkinrange(modifyairplane.getChkinrange())
                .build();

        airplaneRepository.save(airplane);

    }

    @Transactional
    public void softDelete(int airplaneCode) {
        Airplane airplane = airplaneRepository.findByairplaneCode(airplaneCode);
        airplane = airplane.toBuilder().isActive("N").build();

        airplaneRepository.save(airplane);

    }
}
