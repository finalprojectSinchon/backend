package com.finalproject.airport.maintenance.repository;

import com.finalproject.airport.maintenance.dto.MaintenanceDTO;
import com.finalproject.airport.maintenance.entity.MaintenanceEntity;
import com.finalproject.airport.maintenance.entity.MaintenanceEquipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceEquipmentRepository  extends JpaRepository<MaintenanceEquipment, Integer> {
    List<MaintenanceEquipment> findBymaintenance(MaintenanceEntity maintenance);
}
