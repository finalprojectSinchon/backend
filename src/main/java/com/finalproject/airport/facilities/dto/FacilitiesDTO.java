package com.finalproject.airport.facilities.dto;

import com.finalproject.airport.facilities.entity.facilitesType;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class FacilitiesDTO {

    private int facilitiesCode;

    private String facilitiesStatus;

    private String facilitiesLocation;

    private String facilitiesName;

    private facilitesType facilitiesType;

    private String facilitiesManager;

}
