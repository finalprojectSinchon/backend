package com.finalproject.airport.facilities.dto;

import com.finalproject.airport.facilities.entity.FacilitesType;
import lombok.*;



@ToString
public class FacilitiesDTO {

    private int facilitiesCode;

    private String facilitiesStatus;

    private String facilitiesLocation;

    private String facilitiesName;

    private FacilitesType facilitiesType;

    private String facilitiesManager;

    private String facilitiesClass;

    private String isActive;
    public FacilitiesDTO() {
    }

    public FacilitiesDTO(int facilitiesCode, String facilitiesStatus, String facilitiesLocation, String facilitiesName, FacilitesType facilitiesType, String facilitiesManager, String facilitiesClass, String isActive) {
        this.facilitiesCode = facilitiesCode;
        this.facilitiesStatus = facilitiesStatus;
        this.facilitiesLocation = facilitiesLocation;
        this.facilitiesName = facilitiesName;
        this.facilitiesType = facilitiesType;
        this.facilitiesManager = facilitiesManager;
        this.facilitiesClass = facilitiesClass;
        this.isActive = isActive;
    }

    public int getFacilitiesCode() {
        return facilitiesCode;
    }

    public void setFacilitiesCode(int facilitiesCode) {
        this.facilitiesCode = facilitiesCode;
    }

    public String getFacilitiesStatus() {
        return facilitiesStatus;
    }

    public void setFacilitiesStatus(String facilitiesStatus) {
        this.facilitiesStatus = facilitiesStatus;
    }

    public String getFacilitiesLocation() {
        return facilitiesLocation;
    }

    public void setFacilitiesLocation(String facilitiesLocation) {
        this.facilitiesLocation = facilitiesLocation;
    }

    public String getFacilitiesName() {
        return facilitiesName;
    }

    public void setFacilitiesName(String facilitiesName) {
        this.facilitiesName = facilitiesName;
    }

    public FacilitesType getFacilitiesType() {
        return facilitiesType;
    }

    public void setFacilitiesType(FacilitesType facilitiesType) {
        this.facilitiesType = facilitiesType;
    }

    public String getFacilitiesManager() {
        return facilitiesManager;
    }

    public void setFacilitiesManager(String facilitiesManager) {
        this.facilitiesManager = facilitiesManager;
    }

    public String getFacilitiesClass() {
        return facilitiesClass;
    }

    public void setFacilitiesClass(String facilitiesClass) {
        this.facilitiesClass = facilitiesClass;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
