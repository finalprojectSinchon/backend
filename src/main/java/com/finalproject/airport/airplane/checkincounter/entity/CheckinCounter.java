package com.finalproject.airport.airplane.checkincounter.entity;

import com.finalproject.airport.airplane.airplane.Entity.DepartureAirplane;
import com.finalproject.airport.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@Entity(name = "checkincounter")
@Table(name = "TBL_CHECKIN_COUNTER")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
public class CheckinCounter extends BaseTimeEntity {

    @Id
    @Column(name = "CHECKINCOUNTER_CODE")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int checkinCounterCode;

    @Column(name = "LOCATION")
    @Enumerated(EnumType.STRING)
    private CheckinCounterLocation location;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private CheckinCounterType type;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "REGISTRATION_DATE")
    private Date registrationDate;

    @Column(name = "LAST_INSPECTION_DATE")
    private Date lastInspectionDate;

    @Column(name = "MANAGER")
    private String manager;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "AIRLINE")
    private String airline;

    @Column(name = "SCHEDULE_DATE_TIME")
    private Timestamp scheduleDateTime;

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
    }

}
