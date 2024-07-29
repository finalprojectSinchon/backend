package com.finalproject.airport.approval.entity;

import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaim;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounter;
import com.finalproject.airport.facilities.entity.FacilitiesEntity;
import com.finalproject.airport.storage.entity.StorageEntity;
import com.finalproject.airport.store.entity.StoreEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "TBL_APPROVAL")
@AllArgsConstructor
@Getter
@ToString
public class ApprovalEntity {

    @Id
    @Column(name = "APPROVAL_CODE", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int approvalCode;

    @Column(name = "APPROVAL_TYPE")
    @Enumerated(EnumType.STRING)
    private ApprovalTypeEntity approvalType;

    @Column(name = "APPROVAL_STATUS")
    @Enumerated(EnumType.STRING)
    private ApprovalStatusEntity approvalStatus;

    @Column(name = "GATE_CODE")
    private Integer gate;


    @Column(name = "CHECKINCOUNTER_CODE")
    private Integer checkinCounter;


    @Column(name = "BAGGAGECLAIM_CODE")
    private Integer baggageClaim;

    @ManyToOne
    @JoinColumn(name = "STORE_ID")
    private StoreEntity store;


    @Column(name = "STORAGE_CODE")
    private Integer storage;

    @ManyToOne
    @JoinColumn(name = "FACILITY_CODE")
    private FacilitiesEntity facilitiesEntity;

    protected ApprovalEntity() {
    }

    public ApprovalEntity(ApprovalTypeEntity approvalType, ApprovalStatusEntity approvalStatus, Integer gate ,Integer checkinCounter,Integer baggageClaim, Integer storage) {
        this.approvalType = approvalType;
        this.approvalStatus = approvalStatus;
        this.gate = gate;
        this.checkinCounter = checkinCounter;
        this.baggageClaim = baggageClaim;
        this.storage = storage;
    }

    public void setApprovalStatus(ApprovalStatusEntity approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
}
