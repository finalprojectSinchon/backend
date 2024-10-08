package com.finalproject.airport.location.dto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ZoneDTO {

    private String zoneType;

    private Integer airportCode;

    private String region;

    private String floor;

    private String location;

}
