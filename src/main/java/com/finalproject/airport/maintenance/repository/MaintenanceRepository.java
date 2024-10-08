package com.finalproject.airport.maintenance.repository;

import com.finalproject.airport.maintenance.entity.MaintenanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceRepository extends JpaRepository<MaintenanceEntity, Integer> {

    MaintenanceEntity findById(int maintenanceCode);

    MaintenanceEntity findBymaintenanceCode(int maintenanceCode);


    List<MaintenanceEntity> findByIsActive(String y);
}
