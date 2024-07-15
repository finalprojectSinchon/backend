package com.finalproject.airport.airplane.gate.dto;

import com.finalproject.airport.airplane.ArrivalAirplaneDTO;
import com.finalproject.airport.airplane.gate.entity.GateType;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class GateDTO {

    private int gateCode;
    private String location;
    private GateType gateType;
    private String status;
    private Date registrationDate;
    private Date lastInspectionDate;
    private String manager;
    private String note;
    private ArrivalAirplaneDTO airplane;

}
