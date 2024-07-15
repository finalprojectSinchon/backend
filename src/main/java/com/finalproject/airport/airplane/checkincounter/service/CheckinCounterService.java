package com.finalproject.airport.airplane.checkincounter.service;

import com.finalproject.airport.airplane.checkincounter.repository.CheckinCounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckinCounterService {

    private final CheckinCounterRepository repository;

    @Autowired
    public CheckinCounterService(CheckinCounterRepository repository){
        this.repository = repository;
    }

}
