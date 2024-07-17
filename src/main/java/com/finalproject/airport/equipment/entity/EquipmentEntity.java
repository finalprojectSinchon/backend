package com.finalproject.airport.equipment.entity;


import com.finalproject.airport.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_equipment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder(toBuilder = true)
public class EquipmentEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    

    private int equipmentCode;

    //장비 이름
    private String equipmentName;

    //장비 가격
    private int equipmentPrice;

    //장비 수량
    private int equipmentQuantity;

    //장비 위치
    private String equipmentLocation;

    //담당자
    private String equipmentManager;

    // 상태
    private String equipmentStatus;


    public EquipmentEntity(String equipmentName, int equipmentPrice, int equipmentQuantity, String equipmentLocation, String equipmentManager, String equipmentStatus) {
        this.equipmentName = equipmentName;
        this.equipmentPrice = equipmentPrice;
        this.equipmentQuantity = equipmentQuantity;
        this.equipmentLocation = equipmentLocation;
        this.equipmentManager = equipmentManager;
        this.equipmentStatus = equipmentStatus;
    }
}
