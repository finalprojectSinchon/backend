package com.finalproject.airport.maintenance.entity;

import com.finalproject.airport.equipment.entity.EquipmentEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_maintenanceEquipment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
public class MaintenanceEquipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maintenanceEquipmentCode;

    @ManyToOne
    @JoinColumn(name = "MAINTENANCE_CODE")
    private MaintenanceEntity maintenance;

    @ManyToOne
    @JoinColumn (name = "EQUIPMENT_CODE")
    private EquipmentEntity equipment;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "price")
    private Integer price;
}
