package com.finalproject.airport.maintenance.dto;

import com.finalproject.airport.equipment.dto.EquipmentDTO;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MaintenanceEquipmentDTO {

    private MaintenanceDTO maintenance;
    private EquipmentDTO equipment;
    private Integer quatity;
    private Integer price;
}
