package com.finalproject.airport.airplane.airplane.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.sql.Timestamp;

@Entity
@Table(name = "TBL_AIRPLANE")
@AllArgsConstructor
@Getter
@ToString
@Builder(toBuilder = true)
public class Airplane {

    @Id
    @Column(name = "AIRPLANE_CODE", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int airplaneCode;

    @Column(name = "AIRLINE")
    private String airline;

    @Column(name = "SCHEDULE_DATE_TIME")
    private Timestamp scheduleDateTime;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "AIRPORT")
    private String airport;

    @Column(name = "FLIGHTID")
    private String flightId;       // 편명

    @Column(name = "CHKINRANGE" )
    private String chkinrange;  // 체크인 카운터
    
    @Column(name = "CAROUEL" )
    private Integer carousel;       // 수화물 수취대 번호

    @Column(name = "GATE_NUMBER")
    private int gatenumber;     // 탑승구 번호

    @Column(name = "TERMINALID")
    private String terminalid;  // 터미널 구분 (ex. P01: 제1 터미널 P02: 탑승동 P03: 제2 터미널 C01 : 화물터미널 남측 C02 : 화물터미널 북측 C03 : 제2 화물터미널)

    @Column(name = "DELAY_TIME")
    private Integer delayTime;

    @Column(name = "ISACTIVE", length = 1, nullable = false)
    private String isActive;

    protected Airplane(){}

}
