package com.finalproject.airport.approval.service;

import com.finalproject.airport.approval.repository.ApprovalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApprovalService {

    private final ApprovalRepository repository;

    @Autowired
    public ApprovalService(ApprovalRepository repository){
        this.repository = repository;
    }
}
