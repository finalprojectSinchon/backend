package com.finalproject.airport.equipment.entity;


import com.finalproject.airport.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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
    @Column(name ="equipmentStatus" )
    private String equipmentStatus;


    @Column(name = "ISACTIVE", length = 1, nullable = false)
    @Setter
    private String isActive;

    @PrePersist
    private void ensureIsActiveDefault() {
        if (this.isActive == null) {
            this.isActive = "Y";
        }
    }



}

