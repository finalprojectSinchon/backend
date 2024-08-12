package com.finalproject.airport.airplane.gate.entity;

import com.finalproject.airport.airplane.airplane.Entity.DepartureAirplane;
import com.finalproject.airport.member.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@Entity(name = "gate")
@Table(name = "TBL_GATE")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
public class Gate {

    @Id
    @Column(name = "GATE_CODE")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer gateCode;

    @Column(name = "LOCATION")
    private Integer location;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private GateType type;

    @Column(name = "STATUS")
    private String status = "사용가능";


    @Column(name = "REGISTRATION_DATE")
    private Date registrationDate;

    @Column(name = "LAST_INSPECTION_DATE")
    private Date lastInspectionDate;

    @Column(name = "MANAGER")
    private String manager;

    @Column(name = "NOTE")
    private String note;

    @ManyToOne
    @JoinColumn(name = "AIRPLANE_CODE")
    private DepartureAirplane departureAirplane;

    @Column(name = "SCHEDULE_DATE_TIME")
    private Timestamp scheduleDateTime;

    @Column(name = "AIRLINE")
    private String airline;

    @Column(name = "ISACTIVE", length = 1, nullable = false)
    private String isActive;


    @ManyToOne
    @JoinColumn(name = "APPROVAL_REQUESTER")
    private UserEntity approvalRequester;

    @Column(name = "FLIGHTID")
    private String flightid;

    @Column(name = "AIRPORT")
    private String airport;


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


    public void updateGate(Timestamp scheduleDateTime, String airline , String status, String airport, String flightid) {
        this.scheduleDateTime = scheduleDateTime;
        this.airline = airline;
//        this.isActive = isActive;
        this.status = status;
        this.airport = airport;
        this.flightid = flightid;


    }



}

