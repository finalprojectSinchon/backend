package com.finalproject.airport.maintenance.entity;

import com.finalproject.airport.common.BaseTimeEntity;
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
    private String maintenanceType;

    //위치
    @Column(name = "maintenance_location")
    private String maintenanceLocation;

    //상태
    @Column(name = "maintenance_status")
    @Enumerated(EnumType.STRING)
    private MaintenanceStatus maintenanceStatus;

    //담당자
    @Column(name = "maintenance_manager")
    private String maintenanceManager;

    //정비 시작일
    @Temporal(TemporalType.DATE)
    @Column(name = "maintenance_start_date")
    private Date maintenanceStartDate;

    //정비 종료일
    @Temporal(TemporalType.DATE)
    @Column(name = "maintenance_end_date")
    private Date maintenanceEndDate;

    //정비 할 때, 사용 장비
    @Column(name = "maintenance_equipment")
    private String maintenanceEquipment;

    //정비할 때, 사용장비 갯수
    @Column(name = "maintenance_number")
    private Long maintenanceNumber;

    //정비할 때 쓰는 장비 비용
    @Column(name = "maintenance_expense")
    private int maintenanceExpense;

    //정비 내용
    @Column(name = "maintenance_details")
    private String maintenanceDetails;

    @Column(name = "ISACTIVE", length = 1, nullable = false)
    private String isActive;

    @PrePersist
    private void ensureIsActiveDefault() {
        if (this.isActive == null) {
            this.isActive = "Y";
        }
    }

    @Column(name = "GATE_CODE")
    private Integer gate;


    @Column(name = "CHECKINCOUNTER_CODE")
    private Integer checkinCounter;


    @Column(name = "BAGGAGECLAIM_CODE")
    private Integer baggageClaim;


    @Column(name = "STORE_ID")
    private Integer store;


    @Column(name = "STORAGE_CODE")
    private Integer storage;

    @Column(name = "FACILITY_CODE")
    private Integer facilities;

    public MaintenanceEntity(Integer gate,Integer checkinCounter, Integer baggageClaim, Integer store, Integer storage, Integer facilities){
        this.gate=gate;
        this.checkinCounter=checkinCounter;
        this.baggageClaim=baggageClaim;
        this.store=store;
        this.storage=storage;
        this.facilities=facilities;
    }
}
