package com.finalproject.airport.facilities.service;

import com.finalproject.airport.facilities.dto.FacilitiesDTO;
import com.finalproject.airport.facilities.entity.FacilitiesEntity;
import com.finalproject.airport.facilities.repository.FacilitiesRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacilitiesService {

    @Autowired
    private final FacilitiesRepository facilitiesRepository;

    private final ModelMapper modelMapper;
    public List<FacilitiesDTO> selectAllFacilities() {


        List<FacilitiesEntity> facilities = facilitiesRepository.findAll();

        return facilities.stream()
                .map(fact -> modelMapper.map(fact, FacilitiesDTO.class)
                ).collect(Collectors.toList());
    }

    public FacilitiesDTO getfailties(String facilitiesCode) {

        FacilitiesEntity facilities = facilitiesRepository.findById(Integer.valueOf(facilitiesCode)).orElse(null);

        return modelMapper.map(facilities, FacilitiesDTO.class);
    }
}
