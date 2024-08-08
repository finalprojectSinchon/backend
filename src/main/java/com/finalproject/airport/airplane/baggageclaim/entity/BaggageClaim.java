package com.finalproject.airport.airplane.baggageclaim.entity;

import com.finalproject.airport.airplane.airplane.Entity.Airplane;
import com.finalproject.airport.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity(name = "baggageclamim")
@Table(name = "TBL_BAGGAGECLAIM")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
public class BaggageClaim extends BaseTimeEntity {

    @Id
    @Column(name = "BAGGAGECLAIM_CODE", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int baggageClaimCode;

    @Column(name = "LOCATION" )
    @Enumerated(EnumType.STRING)
    private BaggageClaimLocation location;

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

    @Column(name = "ISACTIVE", length = 1, nullable = false)
    private String isActive;

    // Getter and Setter
    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

//
//    @PrePersist
//    private void ensureIsActiveDefault() {
//        if (this.isActive == null) {
//            this.isActive = "Y";
//        }
//    }

}
