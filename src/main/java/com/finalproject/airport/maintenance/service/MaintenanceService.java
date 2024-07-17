package com.finalproject.airport.maintenance.service;

import com.finalproject.airport.maintenance.dto.MaintenanceDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MaintenanceService {

    private final List<MaintenanceDTO> maintenanceList = new ArrayList<>();

    public List<MaintenanceDTO> getMaintenanceList() {
        return maintenanceList;
    }


    public MaintenanceDTO getMaintenanceById(int maintenanceCode) {
        for (MaintenanceDTO maintenance : maintenanceList) {
            if (maintenance.getMaintenanceCode() == maintenanceCode) {
                return maintenance;
            }
        }
        return null;
    }

    public MaintenanceDTO addMaintenance(MaintenanceDTO maintenanceDTO) {
        maintenanceList.add(maintenanceDTO);
        return maintenanceDTO;
    }
}
