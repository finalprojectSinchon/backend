package com.finalproject.airport.airplane.baggageclaim.dto;

import com.finalproject.airport.airplane.airplane.DTO.ArrivalAirplaneDTO;
import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaimIsUse;
import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaimLocation;
import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaimType;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BaggageClaimDTO {

    private int baggageClaimCode;
    private BaggageClaimLocation location;
    private BaggageClaimType type;
    private String status;
    private Date registrationDate;
    private Date lastInspectionDate;
    private String manager;
    private String note;
    private ArrivalAirplaneDTO airplane;
    private char isActive;
    private BaggageClaimIsUse isUse;


}
