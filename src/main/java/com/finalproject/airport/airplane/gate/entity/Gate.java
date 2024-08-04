package com.finalproject.airport.airplane.gate.entity;

import com.finalproject.airport.airplane.airplane.Entity.Airplane;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@Entity(name = "gate")
@Table(name = "TBL_GATE")
@AllArgsConstructor
@NoArgsConstructor
@Getter
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
    private String status;

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
    private Airplane airplane;

    @Column(name = "SCHEDULE_DATE_TIME")
    private Timestamp scheduleDateTime;

    @Column(name = "AIRLINE")
    private String airline;

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


    public void updateGate(Airplane closestAirplane, Timestamp scheduleDateTime, String airline, String y) {
    }
}
