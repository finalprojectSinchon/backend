package com.finalproject.airport.airplane.gate.service;

import com.finalproject.airport.airplane.gate.repository.GateRepository;
import org.springframework.stereotype.Service;

@Service
public class GateService {

    private final GateRepository gateRepository;

    public GateService(GateRepository gateRepository){
        this.gateRepository = gateRepository;
    }
}
