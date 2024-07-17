package com.finalproject.airport.airplane.baggageclaim.controller;

import com.finalproject.airport.airplane.baggageclaim.service.BaggageClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/airplane")
public class BaggageClaimController {

    private final BaggageClaimService service;

    @Autowired
    public BaggageClaimController(BaggageClaimService service){
        this.service = service;
    }





}