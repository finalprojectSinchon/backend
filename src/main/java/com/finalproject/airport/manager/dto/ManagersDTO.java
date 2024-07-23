package com.finalproject.airport.manager.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ManagersDTO {

    private int managersCode;
    private int checkinCounterCode;
    private int airplaneCode;
    private int facilitiesCode;
    private int storageCode;
    private int storeId;
    private int inspectionCode;
    private int baggageClaimCode;

    private int userCode;

    private String isActive;
}
