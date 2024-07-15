package com.finalproject.airport.airplane;

import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AirplaneDTO {

    private int airplaneCode;
    private String airLine;
    private Timestamp departureTime;
    private Timestamp arrivalTime;
    private int delayTime;
    private String departureAirport;
    private String arrivalAirport;
}
