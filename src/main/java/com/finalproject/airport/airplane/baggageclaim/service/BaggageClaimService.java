package com.finalproject.airport.airplane.baggageclaim.service;


import com.finalproject.airport.airplane.baggageclaim.repository.BaggageClaimRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaggageClaimService {

    private final BaggageClaimRepository repository;

    @Autowired
    public BaggageClaimService(BaggageClaimRepository repository){
        this.repository = repository;
    }

}
