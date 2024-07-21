package com.finalproject.airport.airplane.gate.dto;

import com.finalproject.airport.airplane.airplane.DTO.ArrivalAirplaneDTO;
import com.finalproject.airport.airplane.gate.entity.GateType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date registrationDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date lastInspectionDate;
    private String manager;
    private String note;
    private ArrivalAirplaneDTO airplane;
    private char isActive;


}
