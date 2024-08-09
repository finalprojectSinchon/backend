package com.finalproject.airport.QRCode.dto;

import com.finalproject.airport.airplane.gate.entity.GateType;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class GateQRDTO {

    private int id;

    private String location;

    private GateType type;
}
