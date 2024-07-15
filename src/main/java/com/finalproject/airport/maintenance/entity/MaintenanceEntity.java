package com.finalproject.airport.maintenance.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "tbl_maintenance")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class MaintenanceEntity {

    @Id
    @GeneratedValue
    @Column(name = "maintenance_code")
    private int maintenanceCode;

    @Column(name = "maintenance_location")
    private Date maintenanceLocation;

    @Column(name = "maintenance_type")
    private String maintenanceType;

    @Column(name = "maintenance_status")
    private String maintenanceStatus;

    @Column(name = "maintenance_manager")
    private String maintenanceManager;

    @Column(name = "maintenance_cost")
    private int maintenanceCost;

    @Column(name = "maintenance_etc")
    private String maintenanceEtc;

    @Column(name = "maintenance_equipmentUsed")
    private String maintenanceEquipmentUsed;



}
