package com.finalproject.airport.QRCode.dto;

import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounterLocation;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounterType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CheckInCounterQRDTO {

    private int id;

    private CheckinCounterLocation location;

    private CheckinCounterType type;
}
