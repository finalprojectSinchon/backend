package com.finalproject.airport.maintenance.service;

import com.finalproject.airport.maintenance.dto.MaintenanceDTO;
import com.finalproject.airport.maintenance.entity.MaintenanceEntity;
import com.finalproject.airport.maintenance.repository.MaintenanceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<MaintenanceDTO> getMaintenanceList() {
        List<MaintenanceEntity> maintenanceList = maintenanceRepository.findAll();
        return maintenanceList.stream()
                .map(maintenance -> modelMapper.map(maintenance, MaintenanceDTO.class))
                .collect(Collectors.toList());
    }

    // 정비 상세 조회
    public MaintenanceDTO getMaintenanceById(int maintenanceCode) {

        MaintenanceEntity maintenanceEntity = maintenanceRepository.findById(maintenanceCode);
        return modelMapper.map(maintenanceEntity, MaintenanceDTO.class);
    }

// 정비 항목 반환
//    public MaintenanceDTO getMaintenanceById(int maintenanceCode) {
//        for (MaintenanceDTO maintenance : maintenanceList) {
//            if (maintenance.getMaintenanceCode() == maintenanceCode) {
//                return maintenance;
//            }
//        }
//        return null;
//    }




//    // 정비 항목 추가/등록
//    public MaintenanceDTO addMaintenance(MaintenanceDTO maintenanceDTO) {
//        maintenanceList.add(maintenanceDTO);
//        return maintenanceDTO;
//    }
//
//    // 정비 항목 수정
//    public MaintenanceDTO updateMaintenance(int maintenanceCode, MaintenanceDTO updatedMaintenanceDTO) {
//        for (int i = 0; i < maintenanceList.size(); i++) {
//            if (maintenanceList.get(i).getMaintenanceCode() == maintenanceCode) {
//                maintenanceList.set(i, updatedMaintenanceDTO);
//                return updatedMaintenanceDTO;
//            }
//        }
//        return null;
//    }
//
//    // 정비 항목 삭제
//    public boolean deleteMaintenance(int maintenanceCode) {
//        return maintenanceList.removeIf(maintenance -> maintenance.getMaintenanceCode() == maintenanceCode);
//    }
//


}
