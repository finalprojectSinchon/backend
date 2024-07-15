package com.finalproject.airport.airplane.gate.controller;

import com.finalproject.airport.airplane.gate.service.GateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/airplane")
public class GateController {

    private final GateService gateService;

    @Autowired
    public GateController(GateService gateService){
        this.gateService = gateService;
    }


}
