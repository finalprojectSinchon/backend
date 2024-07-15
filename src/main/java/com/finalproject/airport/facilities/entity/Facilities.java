package com.finalproject.airport.facilities.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_facilites")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder(toBuilder = true)
public class Facilities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int facilitiesCode; // 편의시설 시설문코드


    private String facilitiesStatus;  // ex) 사용 , 점검중 , 중단


    private String facilitiesLocation;  //ex) 이동수단


    private String facilitiesName;   //ex)이동수단 = 2층 화장실 , 편의시설 = 화장실 남,여


    private String facilitiesType; //ex)이동수단 = 엘베,에스컬, 무빙워크  , 편이시설 = 화장실


    private String facilitiesManager;  // 담당자


    private String facilitiesClass; // 편의시설 구분

    // 등록일은 시설물에서 관리
}
