package com.finalproject.airport.equipment.dto;


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

    //담당자
    private String manager;

    // 상태
    private String status;

    // location PK
    private int zoneCode;


}
