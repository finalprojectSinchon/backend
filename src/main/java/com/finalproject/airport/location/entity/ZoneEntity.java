package com.finalproject.airport.location.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "tbl_zone")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ZoneEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer zoneCode;

    private String region;

    private String floor;

    private String location;

}

