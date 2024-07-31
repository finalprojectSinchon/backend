package com.finalproject.airport.facilities.entity;

import com.finalproject.airport.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_facilites")
@Getter
@ToString
@Builder(toBuilder = true)
public class FacilitiesEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int facilitiesCode; // 편의시설 시설물코드

    private String status;  // 시설물상태  ex) 사용 , 점검중 , 중단

    private String location;  // 위치   ex) 이동수단

    private String facilitiesName;   // 시설물이름   ex)이동수단 = 2층 화장실 , 편의시설 = 화장실 남,여

    @Enumerated(EnumType.STRING)
    private FacilitesType facilitiesType; // 시설물종류  ex)이동수단 = 엘베,에스컬, 무빙워크  , 편의시설 = 화장실

    private String manager;  // 담당자

    private String facilitiesClass; // 편의시설 구분   ex) 편의시설 , 이동수단

    @Column(name = "ISACTIVE", length = 1, nullable = false)
    private String isActive;


    // Getter and Setter
    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    @PrePersist
    private void ensureIsActiveDefault() {
        if (this.isActive == null) {
            this.isActive = "Y";
        }
        if (this.createdDate == null) {
            this.createdDate = LocalDateTime.now();
        }
    }

    private LocalDateTime createdDate; // 등록일은 시설물에서 관리

    public FacilitiesEntity() {
    }

    public FacilitiesEntity(int facilitiesCode, String status, String location, String facilitiesName, FacilitesType facilitiesType, String manager, String facilitiesClass, String isActive, LocalDateTime createdDate) {
        this.facilitiesCode = facilitiesCode;
        this.status = status;
        this.location = location;
        this.facilitiesName = facilitiesName;
        this.facilitiesType = facilitiesType;
        this.manager = manager;
        this.facilitiesClass = facilitiesClass;
        this.isActive = isActive;
        this.createdDate = createdDate;
    }
}
