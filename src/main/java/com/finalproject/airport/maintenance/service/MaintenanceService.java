package com.finalproject.airport.maintenance.service;

import com.finalproject.airport.maintenance.dto.MaintenanceDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MaintenanceService {

    // 예시 데이터로 정비 항목 목록을 반환하는 메서드
    public List<MaintenanceDTO> getMaintenanceList() {
        List<MaintenanceDTO> maintenanceList = new ArrayList<>();
        return maintenanceList;
    }

    // 특정 코드에 대한 정비 항목을 반환하는 메서드
    public MaintenanceDTO getMaintenanceById(int maintenanceCode) {
        List<MaintenanceDTO> maintenanceList = getMaintenanceList();
        for (MaintenanceDTO maintenance : maintenanceList) {
            if (maintenance.getMaintenanceCode() == maintenanceCode) {
                return maintenance;
            }
        }
        return null; // 해당 코드의 정비 항목이 없을 경우 null 반환
    }
}
