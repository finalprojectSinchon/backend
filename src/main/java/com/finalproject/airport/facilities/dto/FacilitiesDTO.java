package com.finalproject.airport.facilities.dto;

import com.finalproject.airport.facilities.entity.FacilitesType;
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

    private FacilitesType facilitiesType;

    private String facilitiesManager;

}
