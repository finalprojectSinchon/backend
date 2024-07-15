package com.finalproject.airport.airplane;

import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DepartureAirplaneDTO {

    private int airplaneCode;
    private String airline;              // 항공사
    private Timestamp scheduleDateTime;  // 예정일자 (출발 예정시간)
    private String remark;               // 운항상태 (출발,결항,지연,탑승중,마감예정,탑승마감,탑승준비)
    private String airport;              // 도착지공항명
    private String flightId;             // 편명
    private String chkinrange;           // 체크인카운터
    private int gatenumber;              // 탑승구 번호
    private String terminalid;           // 터미널 구분 (ex. P01: 제1 터미널 P02: 탑승동 P03: 제2 터미널 C01 : 화물터미널 남측 C02 : 화물터미널 북측 C03 : 제2 화물터미널)
}
