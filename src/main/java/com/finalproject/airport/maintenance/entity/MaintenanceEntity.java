package com.finalproject.airport.maintenance.entity;

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
@Table(name = "tbl_maintenance")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
public class MaintenanceEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maintenance_code")
    private int maintenanceCode;

    //시설물명
    @Column(name = "maintenance_structure")
    private String maintenanceStructure;

    //시설물 타입
    @Column(name = "maintenance_type")
    private String type;

    //위치
    @Column(name = "maintenance_location")
    private String location;

    //상태
    @Column(name = "maintenance_status")
    @Enumerated(EnumType.STRING)
    private MaintenanceStatus status;

    //담당자
    @Column(name = "maintenance_manager")
    private String manager;

    //정비 시작일
    @Temporal(TemporalType.DATE)
    @Column(name = "maintenance_start_date")
    private Date maintenanceStartDate;

    //정비 종료일
    @Temporal(TemporalType.DATE)
    @Column(name = "maintenance_end_date")
    private Date maintenanceEndDate;



    //정비 내용
    @Column(name = "maintenance_details")
    private String maintenanceDetails;

    @Column(name = "ISACTIVE", length = 1, nullable = false)
    private String isActive;

    @Column(name = "PRICE")
    private Integer price;

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
