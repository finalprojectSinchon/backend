package com.finalproject.airport.airplane.gate.dto;

import com.finalproject.airport.airplane.airplane.DTO.AirplaneDTO;
import com.finalproject.airport.airplane.airplane.DTO.ArrivalAirplaneDTO;
import com.finalproject.airport.airplane.gate.entity.GateType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Schema(description = "탑승구 관련 DTO")
public class GateDTO {

    @Schema(description = "탑승구 정보(PK)")
    private Integer gateCode;

    @Schema(description = "탑승구 위치")
    private Integer location;

    @Schema(description = "탑승구 타입")
    private GateType gateType;

    @Schema(description = "탑승구 상태(정상, 고장, 점검중)")
    private String status;

    @Schema(description = "탑승구 등록 날짜")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date registrationDate;

    @Schema(description = "최근 점검 날짜")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date lastInspectionDate;

    @Schema(description = "담당자")
    private String manager;

    @Schema(description = "비고")
    private String note;

    @Schema(description = "출발 비행기 정보(FK)")
    private AirplaneDTO airplane;

    @Schema(description = "비행기 코드")
    private int airplaneCode;

    @Schema(description = "지연 시간")
    private Integer delayTime;

    @Schema(description = "항공사")
    private String airline;

    @Schema(description = "탑승구 오픈시간")
    private Timestamp scheduleDateTime;

    @Schema(description = "편명")
    private String flightid;

    @Schema(description = "도착공항명")
    private String airport;
}
