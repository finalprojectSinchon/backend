package com.finalproject.airport.airplane.checkincounter.dto;

import com.finalproject.airport.airplane.AirplaneDTO;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounterType;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CheckinCounterDTO {

    private int checkinCounterCode;
    private String location;
    private CheckinCounterType type;
    private String status;
    private Date registrationDate;
    private Date lastInspectionDate;
    private String manager;
    private String note;
    private AirplaneDTO airplane;
}
