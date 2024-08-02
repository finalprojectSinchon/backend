package com.finalproject.airport.airplane.airplane.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Schema(description = "비행기 관련 DTO ")
public class AirplaneDTO {

    @Schema(description = "비행기 정보(PK)")
    private int airplaneCode;

    @Schema(description = "항공사")
    private String airline;              // 항공사

    @Schema(description = "출발/도착 예정시간")
    private Timestamp scheduleDateTime;  // 예정일자 (출발/도착 예정시간)

    @Schema(description = "운항상태 (도착,결항,지연,회항,착륙,출발,탑승중,마감예정,탑승마감,탑승준비)")
    private String remark;               // 운항상태 (도착,결항,지연,회항,착륙,출발,탑승중,마감예정,탑승마감,탑승준비)

    @Schema(description = "출발/도착 공항명")
    private String airport;              // 도착/출발지공항명

    @Schema(description = "편명")
    private String flightId;             // 편명

    @Schema(description = "수하물 수취대 위치")
    private String carousel;             // 수화물 수취대 번호

    @Schema(description = "탑승구 위치")
    private int gatenumber;              // 탑승구 번호

    @Schema(description = "터미널 구분 (ex. P01: 제1 터미널 P02: 탑승동 P03: 제2 터미널 C01 : 화물터미널 남측 C02 : 화물터미널 북측 C03 : 제2 화물터미널)")
    private String terminalid;           // 터미널 구분 (ex. P01: 제1 터미널 P02: 탑승동 P03: 제2 터미널 C01 : 화물터미널 남측 C02 : 화물터미널 북측 C03 : 제2 화물터미널)

    @Schema(description = "체크인 카운터 위치")
    private String chkinrange;           // 체크인카운터

    private String isActive;

    public AirplaneDTO(String airline, String flightId, Timestamp scheduleDateTime, String airport, String remark, String carousel, String gatenumber, String terminalid) {
        this.airline = airline;
        this.flightId = flightId;
        this.scheduleDateTime = scheduleDateTime;
        this.gatenumber = Integer.parseInt(gatenumber);
        this.terminalid = terminalid;
        this.airport = airport;
        this.remark = remark;
        this.carousel = carousel;
        this.isActive = "Y";
    }
    public AirplaneDTO(String airline, String flightId, String chkinrange, Timestamp scheduleDateTime, String airport, String remark,  String gatenumber, String terminalid) {
        this.airline = airline;
        this.flightId = flightId;
        this.scheduleDateTime = scheduleDateTime;
        this.gatenumber = Integer.parseInt(gatenumber);
        this.terminalid = terminalid;
        this.airport = airport;
        this.remark = remark;
        this.chkinrange = chkinrange;
        this.isActive = "Y";
    }

}
