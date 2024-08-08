package com.finalproject.airport.approval.entity;

import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaim;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounter;
import com.finalproject.airport.airplane.gate.entity.Gate;
import com.finalproject.airport.facilities.entity.FacilitiesEntity;
import com.finalproject.airport.storage.entity.StorageEntity;
import com.finalproject.airport.store.entity.StoreEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "TBL_APPROVAL")
@AllArgsConstructor
@Getter
@ToString
@Builder(toBuilder = true)
public class ApprovalEntity {

    @Id
    @Column(name = "APPROVAL_CODE", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int approvalCode;

    @Column(name = "APPROVAL_TYPE")
    @Enumerated(EnumType.STRING)
    private ApprovalTypeEntity type;

    @Column(name = "APPROVAL_STATUS")
    @Enumerated(EnumType.STRING)
    private ApprovalStatusEntity status;

    @Column(name = "CODE")
    @JoinColumn
    private Integer code;

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

    private Integer originalBaggageClaimCode; // 수정 전의 원래 수화물 수취대 코드 필드 추가

    // getter 및 setter 메서드 추가
    public Integer getOriginalBaggageClaimCode() {
        return originalBaggageClaimCode;
    }

    public void setOriginalBaggageClaimCode(Integer originalBaggageClaimCode) {
        this.originalBaggageClaimCode = originalBaggageClaimCode;
    }

    protected ApprovalEntity() {
    }

    public ApprovalEntity(ApprovalTypeEntity type, ApprovalStatusEntity status, Gate gate ,CheckinCounter checkinCounter,BaggageClaim baggageClaim, StorageEntity storage, FacilitiesEntity facilities/* Integer code*/) {
        this.type = type;
        this.status = status;
        this.gate = gate;
        this.checkinCounter = checkinCounter;
        this.baggageClaim = baggageClaim;
        this.storage = storage;
        this.facilities = facilities;
        //this.code = code;
    }

    public ApprovalEntity(ApprovalTypeEntity type, ApprovalStatusEntity status,Gate gate,CheckinCounter checkinCounter,BaggageClaim baggageClaim, StorageEntity storage, FacilitiesEntity facilities, StoreEntity store,int code) {
        this.type = type;
        this.status = status;
        this.gate = gate;
        this.checkinCounter = checkinCounter;
        this.baggageClaim = baggageClaim;
        this.storage = storage;
        this.facilities = facilities;
        this.store = store;
        this.code = code;
    }

    public void setApprovalStatus(ApprovalStatusEntity status) {
        this.status = status;
    }
}
