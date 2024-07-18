package com.finalproject.airport.inspection.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class InspectionDTO {

    private int inspectionCode;

    private String manager;

    private String location;


    private String type;

    private String status;

}
