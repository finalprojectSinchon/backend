package com.finalproject.airport.airplane;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.sql.Timestamp;

@Entity
@Table(name = "TBL_AIRPLANE")
@AllArgsConstructor
@Getter
@ToString
public class Airplane {

    @Id
    @Column(name = "AIRPLANE_CODE", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int airplaneCode;

    @Column(name = "AIRLINE")
    private String airline;

    @Column(name = "DEPARTURE_TIME")
    private Timestamp departureTime;

    @Column(name = "ARRIVAL_TIME")
    private Timestamp arrivalTime;

    @Column(name = "DELAY_TIME")
    private int delayTime;

    @Column(name = "DEPARTURE_AIRPORT")
    private String departureAirport;

    @Column(name = "ARRIVAL_AIRPORT")
    private String arrivalAirport;

    protected Airplane(){}

}
