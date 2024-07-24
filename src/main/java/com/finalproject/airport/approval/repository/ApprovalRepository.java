package com.finalproject.airport.approval.repository;

import com.finalproject.airport.approval.entity.ApprovalEntity;
import com.finalproject.airport.maintenance.entity.MaintenanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovalRepository extends JpaRepository<ApprovalEntity, Integer> {

    ApprovalEntity findById(int ApprovalCode);

    ApprovalEntity findByApprovalCode(int maintenanceCode);

}

