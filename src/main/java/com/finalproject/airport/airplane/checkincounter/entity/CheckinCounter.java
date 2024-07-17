package com.finalproject.airport.airplane.checkincounter.entity;

import com.finalproject.airport.airplane.Airplane;
import com.finalproject.airport.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "TBL_CHECKIN_COUNTER")
@AllArgsConstructor
@Getter
@ToString
public class CheckinCounter extends BaseTimeEntity {

    @Id
    @Column(name = "CHECKINCOUNTER_CODE")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int checkinCounterCode;

    @Column(name = "LOCATION")
    private String location;

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

    @ManyToOne
    @JoinColumn(name = "AIRPLANE_CODE")
    private Airplane airplane;

    protected CheckinCounter(){}

}
