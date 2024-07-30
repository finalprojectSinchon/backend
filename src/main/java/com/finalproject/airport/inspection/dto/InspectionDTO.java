package com.finalproject.airport.inspection.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class InspectionDTO {

    private Integer inspectionCode;

    private String manager;

    private String location;

    private Date regularInspectionDate;

    private String type;

    private String status;

    private Integer phone;

    private String text;

    private String isActive;

}
