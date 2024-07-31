package com.finalproject.airport.facilities.dto;

import com.finalproject.airport.facilities.entity.FacilitesType;
import lombok.*;

import java.time.LocalDateTime;

@ToString
public class FacilitiesDTO {

    private int facilitiesCode;

    private String status;

    private String location;

    private String facilitiesName;

    private FacilitesType type;

    private String manager;

    private String facilitiesClass;

    private String isActive;

    private LocalDateTime createdDate;

    public FacilitiesDTO(int facilitiesCode, String status, String location, String facilitiesName, FacilitesType type, String manager, String facilitiesClass, String isActive, LocalDateTime createdDate) {
        this.facilitiesCode = facilitiesCode;
        this.status = status;
        this.location = location;
        this.facilitiesName = facilitiesName;
        this.type = type;
        this.manager = manager;
        this.facilitiesClass = facilitiesClass;
        this.isActive = isActive;
        this.createdDate = createdDate;
    }

    public int getFacilitiesCode() {
        return facilitiesCode;
    }

    public String getStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }

    public String getFacilitiesName() {
        return facilitiesName;
    }

    public FacilitesType getType() {
        return type;
    }

    public String getManager() {
        return manager;
    }

    public String getFacilitiesClass() {
        return facilitiesClass;
    }

    public String getIsActive() {
        return isActive;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public FacilitiesDTO() {


    }
    }

