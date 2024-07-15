package com.finalproject.airport.equipment.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_equipment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString


public class EquipmentEntity {

    //장비 코드
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_code")
    private int equipmentCode;

    //장비 이름
    @Column(name = "equipment_name")
    private String equipmentName;

    //장비 가격
    @Column(name = "equipment_price")
    private int equipmentPrice;


    //장비 수량
    @Column(name = "equipment_quantity")
    private int equipmentQuantity;

    //장비 위치
    @Column(name = "equipment_location")
    private String equipmentLocation;

    //담당자
    @Column(name = "equipment_assign")
    private String equipmentAssign;




}
