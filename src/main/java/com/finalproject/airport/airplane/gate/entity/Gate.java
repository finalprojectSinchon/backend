package com.finalproject.airport.airplane.gate.entity;

import com.finalproject.airport.airplane.Airplane;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "TBL_GATE")
@AllArgsConstructor
@Getter
@ToString
@Builder(toBuilder = true)
public class Gate {

    @Id
    @Column(name = "GATE_CODE", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int gateCode;

    @Column(name = "LOCATION")
    private String location;

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


    @Column(name = "ISACTIVE", length = 1, nullable = false)
    private String isActive;



    @PrePersist
    private void ensureIsActiveDefault() {
        if (this.isActive == null) {
            this.isActive = "Y";
        }
    }
    protected Gate(){}

}
