package com.finalproject.airport.maintenance.repository;

import com.finalproject.airport.maintenance.entity.MaintenanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceRepository extends JpaRepository<MaintenanceEntity, Integer> {
}
