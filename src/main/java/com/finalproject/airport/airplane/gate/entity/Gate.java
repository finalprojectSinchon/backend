package com.finalproject.airport.airplane.gate.entity;

import com.finalproject.airport.airplane.Airplane;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "TBL_GATE")
@AllArgsConstructor
@Getter
@ToString
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

    protected Gate(){}

}