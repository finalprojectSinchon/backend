package com.finalproject.airport.airplane.baggageclaim.dto;

import com.finalproject.airport.airplane.airplane.DTO.AirplaneDTO;
import com.finalproject.airport.airplane.airplane.DTO.ArrivalAirplaneDTO;
import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaimLocation;
import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaimType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Schema(description = "수화물 수취대 관련 DTO")
public class BaggageClaimDTO {

    @Schema(description = "수화물 수취대 정보(PK)")
    private int baggageClaimCode;

    @Schema(description = "수화물 수취대 위치")
    private BaggageClaimLocation location;

    @Schema(description = "수화물 수취대 타입")
    private BaggageClaimType type;

    @Schema(description = "수화물 수취대 상태(정상,고장,점검중)")
    private String status;

    @Schema(description = "수화물 수취대 등록 날짜")
    private Date registrationDate;

    @Schema(description = "최근 점검 날짜")
    private Date lastInspectionDate;

    @Schema(description = "담당자")
    private String manager;

    @Schema(description = "비고")
    private String note;

    @Schema(description = "비행기 정보(FK)")
    private AirplaneDTO airplane;

    @Schema(description = "비행기 코드")
    private int airplaneCode;
    
    @Schema(description = "지연 시간")
    private Integer delayTime;

    @Schema(description = "비행기 도착시간")
    private Timestamp scheduleDateTime;

}
