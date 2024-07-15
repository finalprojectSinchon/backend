package com.finalproject.airport.airplane.baggageclaim.entity;

import com.finalproject.airport.airplane.Airplane;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "TBL_BAGGAGECLAIM")
@AllArgsConstructor
@Getter
@ToString
public class BaggageClaim {

    @Id
    @Column(name = "BAGGAGECLAIM_CODE", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int baggageClaimCode;

    @Column(name = "LOCATION" )
    private String location;

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

    @ManyToOne
    @JoinColumn(name = "AIRPLANE_CODE")
    private Airplane airplane;

    protected BaggageClaim(){}
}