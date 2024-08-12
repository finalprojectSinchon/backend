package com.finalproject.airport.equipment.service;

import com.finalproject.airport.equipment.dto.EquipmentDTO;
import com.finalproject.airport.equipment.dto.EquipmentStorageDTO;
import com.finalproject.airport.equipment.entity.EquipmentEntity;
import com.finalproject.airport.equipment.repository.EquipmentRepository;
import com.finalproject.airport.location.service.LocationService;
import com.finalproject.airport.storage.entity.StorageEntity;
import com.finalproject.airport.storage.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final ModelMapper modelMapper;
    private final LocationService locationService;
    private final StorageRepository storageRepository;

    public List<EquipmentDTO> findAllEquipment() {
        log.info("Fetching all active equipment");

        List<EquipmentEntity> equipmentList = equipmentRepository.findByIsActive("Y");
        return equipmentList.stream()
                .map(equipmentEntity -> modelMapper.map(equipmentEntity, EquipmentDTO.class))
                .collect(Collectors.toList());
    }

    public EquipmentDTO findByCode(int equipmentCode) {
        log.info("Fetching equipment with code: {}", equipmentCode);

        EquipmentEntity equipment = equipmentRepository.findById(equipmentCode)
                .orElseThrow(() -> new IllegalArgumentException("No equipment found with code: " + equipmentCode));
        return modelMapper.map(equipment, EquipmentDTO.class);
    }

    @Transactional
    public void addEquipment(EquipmentDTO equipmentDTO) {
        log.info("Adding new equipment: {}", equipmentDTO);

        EquipmentEntity equipmentEntity = modelMapper.map(equipmentDTO, EquipmentEntity.class);
        equipmentRepository.save(equipmentEntity);
    }

    @Transactional
    public void updateEquipment(int equipmentCode, EquipmentDTO equipmentDTO) {
        log.info("Updating equipment with code: {}", equipmentCode);

        EquipmentEntity equipment = equipmentRepository.findByequipmentCode(equipmentCode);
        if (equipment == null) {
            throw new IllegalArgumentException("No equipment found with code: " + equipmentCode);
        }

        equipment = equipment.toBuilder()
                .equipmentName(equipmentDTO.getEquipmentName())
                .equipmentPrice(equipmentDTO.getEquipmentPrice())
                .equipmentQuantity(equipmentDTO.getEquipmentQuantity())
                .location(equipmentDTO.getLocation())
                .category(equipmentDTO.getCategory())
                .build();

        equipmentRepository.save(equipment);
    }

    @Transactional
    public void softDeleteEquipment(int equipmentCode) {
        log.info("Soft deleting equipment with code: {}", equipmentCode);

        EquipmentEntity equipment = equipmentRepository.findById(equipmentCode)
                .orElseThrow(() -> new IllegalArgumentException("No equipment found with code: " + equipmentCode));
        equipment = equipment.toBuilder().isActive("N").build();
        equipmentRepository.save(equipment);
    }

    @Transactional
    public void insertEquipment(EquipmentDTO equipmentDTO) {
        log.info("Inserting equipment: {}", equipmentDTO);

        try {
            EquipmentEntity insertEquipment = modelMapper.map(equipmentDTO, EquipmentEntity.class);
            EquipmentEntity savedEquipment = equipmentRepository.save(insertEquipment);

            Integer equipmentId = savedEquipment.getEquipmentCode();
            locationService.registEquipment(equipmentId, equipmentDTO.getZoneCode());

        } catch (Exception e) {
            log.error("Error inserting equipment", e);
            throw new RuntimeException("Error inserting equipment", e);
        }
    }

    public List<EquipmentStorageDTO> getStorage() {

        List<StorageEntity> storageList = storageRepository.findAll();

        List<EquipmentStorageDTO> equipmentStorageDTOS = storageList.stream()
                .map(storageEntity -> {
                    EquipmentStorageDTO dto = new EquipmentStorageDTO();
                    dto.setCode(storageEntity.getStorageCode());
                    dto.setLocation(storageEntity.getLocation());
                    return dto;
                })
                .collect(Collectors.toMap(
                        EquipmentStorageDTO::getLocation,
                        dto -> dto,
                        (existing, replacement) -> existing
                ))
                .values()
                .stream()
                .collect(Collectors.toList());

        return equipmentStorageDTOS;
    }
}
