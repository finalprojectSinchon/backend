package com.finalproject.airport.equipment.dto;


import com.finalproject.airport.equipment.entity.EquipmentCategory;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EquipmentDTO {

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
    private EquipmentCategory category;

    // 상태
    private String status;

    // location PK
    private int zoneCode;

    private String img;

}
