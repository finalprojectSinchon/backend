package com.finalproject.airport.airplane.airplane.DTO;

import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ArrivalAirplaneDTO {

    private int airplaneCode;
    private String airline;              // 항공사
    private Timestamp scheduleDateTime;  // 예정일자 (도착 예정시간)
    private String remark;               // 운항상태 (도착,결항,지연,회항,착륙)
    private String airport;              // 출발지공항명
    private String flightId;             // 편명
    private int carousel;                // 수화물 수취대 번호
    private int gatenumber;              // 탑승구 번호
    private String terminalid;           // 터미널 구분 (ex. P01: 제1 터미널 P02: 탑승동 P03: 제2 터미널 C01 : 화물터미널 남측 C02 : 화물터미널 북측 C03 : 제2 화물터미널)


}
