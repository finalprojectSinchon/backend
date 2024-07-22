package com.finalproject.airport.maintenance.service;

import com.finalproject.airport.maintenance.dto.MaintenanceDTO;
import com.finalproject.airport.maintenance.entity.MaintenanceEntity;
import com.finalproject.airport.maintenance.repository.MaintenanceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MaintenanceService(MaintenanceRepository maintenanceRepository , ModelMapper modelMapper) {
        this.maintenanceRepository = maintenanceRepository;
        this.modelMapper = modelMapper;
    }

    // 정비 전체 조회
    public List<MaintenanceDTO> findAll() {
        List<MaintenanceEntity> maintenanceList = maintenanceRepository.findByIsActive("Y");
        return maintenanceList.stream()
                .map(maintenance -> modelMapper.map(maintenance, MaintenanceDTO.class))
                .collect(Collectors.toList());
    }

    // 정비 상세 조회
    public MaintenanceDTO getMaintenanceById(int maintenanceCode) {

        MaintenanceEntity maintenanceEntity = maintenanceRepository.findById(maintenanceCode);
        return modelMapper.map(maintenanceEntity, MaintenanceDTO.class);
    }

    // 정비 항목 수정
    @Transactional
    public void updateMaintenance(int maintenanceCode, MaintenanceDTO maintenanceDTO) {

        MaintenanceEntity maintenanceEntity = maintenanceRepository.findBymaintenanceCode(maintenanceCode);

        maintenanceEntity = maintenanceEntity.toBuilder()
                .maintenanceStructure(maintenanceDTO.getStructure())
                .maintenanceType(maintenanceDTO.getType())
                .maintenanceLocation(maintenanceDTO.getLocation())
                .maintenanceStatus(maintenanceDTO.getStatus())
                .maintenanceManager(maintenanceDTO.getManager())
                .maintenanceEquipment(maintenanceDTO.getEquipment())
                .maintenanceNumber(maintenanceDTO.getNumber())
                .maintenanceExpense(maintenanceDTO.getExpense())
                .maintenanceStartDate(maintenanceDTO.getMaintenanceStartDate())
                .maintenanceEndDate(maintenanceDTO.getMaintenanceEndDate())
                .maintenanceDetails(maintenanceDTO.getMaintenanceDetails())
                .build();

        maintenanceRepository.save(maintenanceEntity);
    }

    // 정비 삭제
    @Transactional
    public void softDelete(int maintenanceCode) {

        MaintenanceEntity maintenanceEntity = maintenanceRepository.findBymaintenanceCode(maintenanceCode);

        maintenanceEntity = maintenanceEntity.toBuilder().isActive("N").build();

        maintenanceRepository.save(maintenanceEntity);
    }

    @Transactional
    public String insertMaintenance(MaintenanceDTO maintenanceDTO) {

        int result = 0;

        try {
            MaintenanceEntity insertMaintenance = modelMapper.map(maintenanceDTO, MaintenanceEntity.class);
            maintenanceRepository.save(insertMaintenance);

            result = 1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return(result > 0)? "정비 등록 성공": "정비 등록 실패";
    }

}
