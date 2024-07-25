package com.finalproject.airport.manager.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ManagerUpdateDTO {

    private int userCode;

    private String airportType;

    private int airportCode;
}
