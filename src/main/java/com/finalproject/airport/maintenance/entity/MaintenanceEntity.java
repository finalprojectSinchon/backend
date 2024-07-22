package com.finalproject.airport.maintenance.entity;

import com.finalproject.airport.common.BaseTimeEntity;
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
    private String maintenanceStatus;

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
}
