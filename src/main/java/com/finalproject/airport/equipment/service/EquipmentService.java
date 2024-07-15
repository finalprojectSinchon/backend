package com.finalproject.airport.equipment.service;

import com.finalproject.airport.equipment.dto.EquipmentDTO;
import com.finalproject.airport.equipment.entity.EquipmentEntity;
import com.finalproject.airport.equipment.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EquipmentService {

    @Autowired
    private final EquipmentRepository equipmentRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<EquipmentDTO> findAllEquipment() {
        List<EquipmentEntity> equipmentList = equipmentRepository.findAll();


        return equipmentList.stream().map(equipmentEntity ->
                modelMapper.map(equipmentEntity, EquipmentDTO.class)).collect(Collectors.toList());
    }

    public EquipmentDTO findByCode(int equipmentCode) {
        EquipmentEntity equipment = equipmentRepository.findById(equipmentCode).orElseThrow(IllegalArgumentException::new);
        return modelMapper.map(equipment, EquipmentDTO.class);
    }

    public void addEquipment(EquipmentDTO equipmentDTO) {
        equipmentDTO.setEquipmentStatus("Y");   // 상태
        System.out.println("equipmentDTO = " + equipmentDTO);
        EquipmentEntity equipmentEntity = modelMapper.map(equipmentDTO, EquipmentEntity.class);
        System.out.println("equipmentEntity = " + equipmentEntity);
        equipmentRepository.save(equipmentEntity);
    }

    public void updateEquipment(int equipmentCode, EquipmentDTO equipmentDTO) {
        equipmentDTO.setEquipmentCode(equipmentCode);
        equipmentDTO.setEquipmentStatus("Y");
        EquipmentEntity equipment = modelMapper.map(equipmentDTO, EquipmentEntity.class);
        equipmentRepository.save(equipment);
    }


    public void softDeleteEquipment(int equipmentCode) {
        EquipmentEntity equipment = equipmentRepository.findById(equipmentCode).orElseThrow(IllegalArgumentException::new);
        equipment = equipment.toBuilder().equipmentStatus("N").build();
        equipmentRepository.save(equipment);
    }
}
