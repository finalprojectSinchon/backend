package com.finalproject.airport.approval.service;

import com.finalproject.airport.approval.dto.ApprovalDTO;
import com.finalproject.airport.approval.entity.ApprovalEntity;
import com.finalproject.airport.approval.repository.ApprovalRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ApprovalService(ApprovalRepository repository, ModelMapper modelMapper) {
        this.approvalRepository = repository;
        this.modelMapper = modelMapper;
    }

    public List<ApprovalEntity> findAll() {
        return approvalRepository.findAll();
    }
}

