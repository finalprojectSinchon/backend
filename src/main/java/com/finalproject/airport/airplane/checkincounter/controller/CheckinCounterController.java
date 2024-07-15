package com.finalproject.airport.airplane.checkincounter.controller;


import com.finalproject.airport.airplane.checkincounter.service.CheckinCounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/airplane")
public class CheckinCounterController {

    private final CheckinCounterService service;

    @Autowired
    public CheckinCounterController(CheckinCounterService service){
        this.service = service;
    }

}
