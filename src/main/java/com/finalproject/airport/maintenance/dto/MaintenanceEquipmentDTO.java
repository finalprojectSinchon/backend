package com.finalproject.airport.maintenance.dto;

import com.finalproject.airport.equipment.dto.EquipmentDTO;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MaintenanceEquipmentDTO {
    private MaintenanceDTO maintenance;
    private List<EquipmentQuantityDTO> equipment;
}
