package com.finalproject.airport.inspection.entity;


import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaim;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounter;
import com.finalproject.airport.airplane.gate.entity.Gate;
import com.finalproject.airport.common.BaseTimeEntity;
import com.finalproject.airport.facilities.entity.FacilitiesEntity;
import com.finalproject.airport.storage.entity.StorageEntity;
import com.finalproject.airport.store.entity.StoreEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Entity
@Table(name = "tbl_inspection")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder(toBuilder = true)


public class InspectionEntity extends BaseTimeEntity {

    @Id
    @Column(name = "inspection_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int inspectionCode;

    @Column(name = "manager")
    private String manager;

    @Column(name = "location")
    private String location;

    @Column(name = "regularInspectionDate")
    private Date regularInspectionDate;

    @Column(name = "lastInscpectionDate")
    private Date lastInspectionDate;

    @Column(name = "type")
    private String type;

    @Column(name = "status")
    private String status;

    @Column(name = "phone")
    private String phone;

    @Column(name = "text")
    private String text;

    @Column(name = "ISACTIVE", length = 1, nullable = false)
    @Setter
    private String isActive;

    @Column(name = "STRUCTURE")
    private String structure;

    @PrePersist
    private void ensureIsActiveDefault() {
        if (this.isActive == null) {
            this.isActive = "Y";
        }
    }
    @ManyToOne
    @JoinColumn(name = "GATE_CODE")
    private Gate gate;

    @ManyToOne
    @JoinColumn(name = "CHECKINCOUNTER_CODE")
    private CheckinCounter checkinCounter;

    @ManyToOne
    @JoinColumn(name = "BAGGAGECLAIM_CODE")
    private BaggageClaim baggageClaim;

    @ManyToOne
    @JoinColumn(name = "STORE_ID")
    private StoreEntity store;

    @ManyToOne
    @JoinColumn(name = "STORAGE_CODE")
    private StorageEntity storage;

    @ManyToOne
    @JoinColumn(name = "FACILITY_CODE")
    private FacilitiesEntity facilities;



}
