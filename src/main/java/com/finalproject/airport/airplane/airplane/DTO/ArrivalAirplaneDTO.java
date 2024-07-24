package com.finalproject.airport.airplane.airplane.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Schema(description = "도착 비행기 관련 DTO")
public class ArrivalAirplaneDTO {

    @Schema(description = "도착 비행기 정보(PK)")
    private int airplaneCode;

    @Schema(description = "항공사")
    private String airline;              // 항공사

    @Schema(description = "도착 예정 시간")
    private Timestamp scheduleDateTime;  // 예정일자 (도착 예정시간)

    @Schema(description = "운항 상태(도착, 결항, 지연, 회항, 착륙)")
    private String remark;               // 운항상태 (도착,결항,지연,회항,착륙)

    @Schema(description = "출발공항명")
    private String airport;              // 출발지공항명

    @Schema(description = "편명")
    private String flightId;             // 편명

    @Schema(description = "수화물 수취대 위치")
    private String carousel;                // 수화물 수취대 번호

    @Schema(description = "탑승구 위치")
    private int gatenumber;              // 탑승구 번호

    @Schema(description = "터미널 구분 (ex. P01: 제1 터미널 P02: 탑승동 P03: 제2 터미널 C01 : 화물터미널 남측 C02 : 화물터미널 북측 C03 : 제2 화물터미널)")
    private String terminalid;           // 터미널 구분 (ex. P01: 제1 터미널 P02: 탑승동 P03: 제2 터미널 C01 : 화물터미널 남측 C02 : 화물터미널 북측 C03 : 제2 화물터미널)


}
