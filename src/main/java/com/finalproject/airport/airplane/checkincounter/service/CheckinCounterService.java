package com.finalproject.airport.airplane.checkincounter.service;

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
    private final ModelMapper modelMapper;

    @Autowired
    public CheckinCounterService(CheckinCounterRepository repository, ModelMapper modelMapper){
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void insertchkinCounter(CheckinCounterDTO chkinCounter) {

        CheckinCounter insertchkinCounter = modelMapper.map(chkinCounter, CheckinCounter.class);

        repository.save(insertchkinCounter);
    }

    public List<CheckinCounterDTO> findAll() {
        List<CheckinCounter> checkinCounters = repository.findAll();

        return checkinCounters.stream()
                .map(chkinCounter -> modelMapper.map(chkinCounter, CheckinCounterDTO.class))
                .collect(Collectors.toList());
    }

    public CheckinCounterDTO findBycheckinCounterCode(int checkinCounterCode) {

        CheckinCounter chkinCounter = repository.findBycheckinCounterCode(checkinCounterCode);
        return modelMapper.map(chkinCounter,CheckinCounterDTO.class);
    }
}
