package com.finalproject.airport.facilities.entity;

import com.finalproject.airport.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_facilites")

@Getter
@ToString
@Builder(toBuilder = true)
public class FacilitiesEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int facilitiesCode; // 편의시설 시설물코드


    private String facilitiesStatus;  // 시설물상태  ex) 사용 , 점검중 , 중단


    private String facilitiesLocation;  // 위치   ex) 이동수단


    private String facilitiesName;   // 시설물이름   ex)이동수단 = 2층 화장실 , 편의시설 = 화장실 남,여

    @Enumerated(EnumType.STRING)
    private FacilitesType facilitiesType; // 시설물종류  ex)이동수단 = 엘베,에스컬, 무빙워크  , 편의시설 = 화장실

    private String facilitiesManager;  // 담당자

    private String facilitiesClass; // 편의시설 구분   ex) 편의시설 , 이동수단

    @Column(name = "ISACTIVE", length = 1, nullable = false)
    private String isActive = "Y";
    @PrePersist
    private void ensureIsActiveDefault() {
        if (this.isActive == null) {
            this.isActive = "Y";
        }
    }

    // 등록일은 시설물에서 관리


    public FacilitiesEntity() {
    }

    public FacilitiesEntity(int facilitiesCode, String facilitiesStatus, String facilitiesLocation, String facilitiesName, FacilitesType facilitiesType, String facilitiesManager, String facilitiesClass, String isActive) {
        this.facilitiesCode = facilitiesCode;
        this.facilitiesStatus = facilitiesStatus;
        this.facilitiesLocation = facilitiesLocation;
        this.facilitiesName = facilitiesName;
        this.facilitiesType = facilitiesType;
        this.facilitiesManager = facilitiesManager;
        this.facilitiesClass = facilitiesClass;
        this.isActive = isActive;
    }
}
