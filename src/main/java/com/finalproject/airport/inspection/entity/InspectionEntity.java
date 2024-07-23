package com.finalproject.airport.inspection.entity;


import com.finalproject.airport.common.BaseTimeEntity;
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

    @Column(name = "ISACTIVE", length = 1, nullable = false)
    private String isActive = "Y";

    @Column(name = "phone")
    private Integer phone;

    @Column(name = "text")
    private String text;


}
