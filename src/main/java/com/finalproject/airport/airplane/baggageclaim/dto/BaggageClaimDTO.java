package com.finalproject.airport.airplane.baggageclaim.dto;

import com.finalproject.airport.airplane.AirplaneDTO;
import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaimType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BaggageClaimDTO {

    private int baggageClaimCode;
    private String location;
    private BaggageClaimType type;
    private String status;
    private Date registrationDate;
    private Date lastInspectionDate;
    private String manager;
    private String note;
    private AirplaneDTO airplane;
}
