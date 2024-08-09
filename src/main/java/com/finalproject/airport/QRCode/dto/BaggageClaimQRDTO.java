package com.finalproject.airport.QRCode.dto;

import com.finalproject.airport.airplane.airplane.DTO.AirplaneDTO;
import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaimLocation;
import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaimType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BaggageClaimQRDTO {

    private int id;

    private BaggageClaimType type;

    private String location;


}
