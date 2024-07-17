package com.finalproject.airport.airplane.checkincounter.service;

import com.finalproject.airport.airplane.checkincounter.dto.CheckinCounterDTO;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounter;
import com.finalproject.airport.airplane.checkincounter.repository.CheckinCounterRepository;
import com.finalproject.airport.airplane.gate.entity.Gate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckinCounterService {

    private final CheckinCounterRepository repository;
    private final ModelMapper modelMapper;

    @Autowired
    public CheckinCounterService(CheckinCounterRepository repository, ModelMapper modelMapper){
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    public void insertchkinCounter(CheckinCounterDTO chkinCounter) {

        CheckinCounter insertchkinCounter = modelMapper.map(chkinCounter, CheckinCounter.class);

        repository.save(insertchkinCounter);
    }
}
