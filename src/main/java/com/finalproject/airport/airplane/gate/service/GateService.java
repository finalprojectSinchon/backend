package com.finalproject.airport.airplane.gate.service;

import com.finalproject.airport.airplane.gate.dto.GateDTO;
import com.finalproject.airport.airplane.gate.entity.Gate;
import com.finalproject.airport.airplane.gate.repository.GateRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GateService {

    private final GateRepository gateRepository;

    private final ModelMapper modelMapper;

    public GateService(GateRepository gateRepository, ModelMapper modelMapper){

        this.gateRepository = gateRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public String insertGate(GateDTO gate) {

        int result = 0;

        try{

            Gate insertGate = modelMapper.map(gate, Gate.class);

            gateRepository.save(insertGate);

            result = 1;
        } catch (Exception e){
            throw new RuntimeException(e);
        }


        return (result > 0)? "탑승구 등록 성공": "탑승구 등록 실패";
    }

    public List<GateDTO> findAll() {

        List<Gate> gateList = gateRepository.findByisActive("Y");

        return gateList.stream()
                .map(gate -> modelMapper.map(gate,GateDTO.class))
                .collect(Collectors.toList());
    }

    public GateDTO findBygateCode(int gateCode) {

        Gate gate = gateRepository.findBygateCode(gateCode);
        return modelMapper.map(gate,GateDTO.class);
    }

    @Transactional
    public void modifyGate(int gateCode, GateDTO modifyGate) {

        Gate gate = gateRepository.findBygateCode(gateCode);


        gate =  gate.toBuilder()
                .manager(modifyGate.getManager())
                .type(modifyGate.getGateType())
                .note(modifyGate.getNote())
                .location(modifyGate.getLocation())
                .status(modifyGate.getStatus())
                .lastInspectionDate(modifyGate.getLastInspectionDate())
                .registrationDate(modifyGate.getRegistrationDate())
                .isUse(modifyGate.getIsUse())
                .build();

        gateRepository.save(gate);

    }

    @Transactional
    public void softDelete(int gateCode) {

       Gate gate = gateRepository.findBygateCode(gateCode);

        gate = gate.toBuilder().isActive("N").build();


       gateRepository.save(gate);

    }
}
