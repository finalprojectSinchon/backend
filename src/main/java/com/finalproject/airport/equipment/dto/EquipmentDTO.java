package com.finalproject.airport.equipment.dto;


import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString


public class EquipmentDTO {

    public int equipmentCode;

    public String equipmentName;

    public int equipmentPrice;

    public String equipmentQuantity;

    public String equipmentLocation;

    public String equipmentAssign;



    public EquipmentDTO(int equipmentCode, String equipmentName, int equipmentPrice, String equipmentQuantity, String equipmentLocation, String equipmentAssign) {
        this.equipmentCode = equipmentCode;
        this.equipmentName = equipmentName;
        this.equipmentPrice = equipmentPrice;
        this.equipmentQuantity = equipmentQuantity;
        this.equipmentLocation = equipmentLocation;
        this.equipmentAssign = equipmentAssign;
    }
}
