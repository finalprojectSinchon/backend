package com.finalproject.airport.airplane.baggageclaim.entity;

import com.finalproject.airport.airplane.airplane.Entity.DepartureAirplane;
import com.finalproject.airport.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@Entity(name = "baggageclamim")
@Table(name = "TBL_BAGGAGECLAIM")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
public class BaggageClaim extends BaseTimeEntity {

    @Id
    @Column(name = "BAGGAGECLAIM_CODE", nullable = false)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int baggageClaimCode;

    @Column(name = "LOCATION" )
    @Enumerated(EnumType.STRING)
    private BaggageClaimLocation location;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private BaggageClaimType type;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "REGISTRAITON_DATE")
    private Date registrationDate;

    @Column(name = "LAST_INSPECTION_DATE")
    private Date lastInspectionDate;

    @Column(name = "MANAGER")
    private String manager;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "SCHEDULE_DATE_TIME")
    private Timestamp scheduleDateTime;

    @Column(name = "ISACTIVE", length = 1, nullable = false)
    private String isActive;

    @Column(name = "AIRLINE")
    private String airline;

    // Getter and Setter
    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public void updateBaggageClaim(Timestamp scheduleDateTime, String airline, String status) {
        this.scheduleDateTime = scheduleDateTime;
        this.status = status;
        this.airline = airline;
    }

    @PrePersist
    private void ensureIsActiveDefault() {
        if (this.isActive == null) {
            this.isActive = "Y";
        }
    }

}
