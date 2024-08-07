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
    private String location;

    //카테고리
    @Enumerated(EnumType.STRING)
    private EquipmentCategory category;

    // 상태
    @Column(name ="equipment_status" )
    private String status;


    @Column(name = "ISACTIVE", length = 1, nullable = false)
    @Setter
    private String isActive;

    @Column(name = "IMG",nullable = false)
    private String img;

    @PrePersist
    private void ensureIsActiveDefault() {
        if (this.isActive == null) {
            this.isActive = "Y";
        }
        if (this.img.isEmpty()) {
            this.img = "https://firebasestorage.googleapis.com/v0/b/springreactfinal.appspot.com/o/equipmentImg%2FEquipment%20pppp?alt=media&token=f46c00bf-6c56-4073-9701-4536715cf3f1";
        }
    }







}

