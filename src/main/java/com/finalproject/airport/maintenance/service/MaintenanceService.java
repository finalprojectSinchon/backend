package com.finalproject.airport.maintenance.service;

import com.finalproject.airport.maintenance.dto.MaintenanceDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MaintenanceService {

    private final List<MaintenanceDTO> maintenanceList = new ArrayList<>();


    // 정비 항목 반환
    public MaintenanceDTO getMaintenanceById(int maintenanceCode) {
        for (MaintenanceDTO maintenance : maintenanceList) {
            if (maintenance.getMaintenanceCode() == maintenanceCode) {
                return maintenance;
            }
        }
        return null;
    }

    public List<MaintenanceDTO> getMaintenanceList() {

        return maintenanceList;
    }

    // 정비 항목 추가/등록
    public MaintenanceDTO addMaintenance(MaintenanceDTO maintenanceDTO) {
        maintenanceList.add(maintenanceDTO);
        return maintenanceDTO;
    }

    // 정비 항목 수정
    public MaintenanceDTO updateMaintenance(int maintenanceCode, MaintenanceDTO updatedMaintenanceDTO) {
        for (int i = 0; i < maintenanceList.size(); i++) {
            if (maintenanceList.get(i).getMaintenanceCode() == maintenanceCode) {
                maintenanceList.set(i, updatedMaintenanceDTO);
                return updatedMaintenanceDTO;
            }
        }
        return null;
    }

    // 정비 항목 삭제
    public boolean deleteMaintenance(int maintenanceCode) {
        return maintenanceList.removeIf(maintenance -> maintenance.getMaintenanceCode() == maintenanceCode);
    }



}
