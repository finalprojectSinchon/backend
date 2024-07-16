package com.finalproject.airport.maintenance.service;

import com.finalproject.airport.maintenance.dto.MaintenanceDTO;
import com.finalproject.airport.maintenance.entity.MaintenanceEntity;
import com.finalproject.airport.maintenance.repository.MaintenanceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<MaintenanceDTO> getMaintenanceList() {
       List<MaintenanceEntity> maintenanceList = maintenanceRepository.findAll();
       List<MaintenanceDTO> maintenanceDTOList = new ArrayList<>();
       maintenanceList.forEach(maintenanceEntity -> maintenanceDTOList.add(modelMapper.map(maintenanceEntity, MaintenanceDTO.class)));

       return maintenanceDTOList;
    }

    public MaintenanceDTO getMaintenance(int maintenanceCode) {
        MaintenanceEntity maintenanceEntity = maintenanceRepository.findById(Integer.valueOf(maintenanceCode)).orElse(null);

        return modelMapper.map(maintenanceEntity, MaintenanceDTO.class);
    }

    public void add(MaintenanceDTO maintenanceDTO) {
        MaintenanceEntity maintenanceEntity = modelMapper.map(maintenanceDTO, MaintenanceEntity.class);
        maintenanceRepository.save(maintenanceEntity);
    }

    public void softDeleteMaintenance(int maintenanceCode) {
        MaintenanceEntity maintenanceEntity = maintenanceRepository.findById(maintenanceCode).orElseThrow(IllegalArgumentException::new);

        maintenanceEntity = maintenanceRepository.findById(maintenanceCode).orElseThrow(IllegalArgumentException::new);
        maintenanceRepository.delete(maintenanceEntity);

    }

    public void updateMaintenance(int maintenanceCode, MaintenanceDTO maintenanceDTO) {
        maintenanceDTO.setMaintenanceCode(maintenanceCode);
        MaintenanceEntity maintenanceEntity = maintenanceRepository.findById(maintenanceCode).orElseThrow(IllegalArgumentException::new);
        maintenanceEntity = modelMapper.map(maintenanceDTO, MaintenanceEntity.class);
        maintenanceRepository.save(maintenanceEntity);
    }

}
