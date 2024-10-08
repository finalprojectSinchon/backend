package com.finalproject.airport.approval.entity;

import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaim;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounter;
import com.finalproject.airport.airplane.gate.entity.Gate;
import com.finalproject.airport.common.BaseTimeEntity;
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
public class ApprovalEntity extends BaseTimeEntity {

    @Id
    @Column(name = "APPROVAL_CODE", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int approvalCode;

    @Column(name = "APPROVAL_TYPE")
    @Enumerated(EnumType.STRING)
    private ApprovalTypeEntity type;

    @Column(name = "APPROVAL_STATUS")
    private String status;

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


    @Column(name="CHECKED")
    private String checked;

    @Column(name = "NOTI")
    private String noti;

    @PrePersist
    private void ensureIsCheckedDefault() {
        if (this.checked == null) {
            this.checked = "N";
        }else if(this.noti == null){
            this.noti = "N";
        }
    }

    protected ApprovalEntity() {
    }

    public ApprovalEntity(ApprovalTypeEntity type, String status, Gate gate ,CheckinCounter checkinCounter,BaggageClaim baggageClaim, StorageEntity storage, FacilitiesEntity facilities,String noti) {
        this.type = type;
        this.status = status;
        this.gate = gate;
        this.checkinCounter = checkinCounter;
        this.baggageClaim = baggageClaim;
        this.storage = storage;
        this.facilities = facilities;
        this.noti = noti;
    }

    public ApprovalEntity(ApprovalTypeEntity type, String status,Gate gate,CheckinCounter checkinCounter,BaggageClaim baggageClaim, StorageEntity storage, FacilitiesEntity facilities, StoreEntity store,Integer code,String noti) {
        this.type = type;
        this.status = status;
        this.gate = gate;
        this.checkinCounter = checkinCounter;
        this.baggageClaim = baggageClaim;
        this.storage = storage;
        this.facilities = facilities;
        this.store = store;
        this.code = code;
        this.noti = noti;
    }

}
