package com.finalproject.airport.approval.controller;

import com.finalproject.airport.approval.service.ApprovalService;
import org.aspectj.lang.annotation.AfterThrowing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/approve")
public class ApprovalController {

    private final ApprovalService service;

    @Autowired
    public ApprovalController(ApprovalService service){
        this.service = service;
    }

}
