package com.finalproject.airport.manager.entity;

import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaim;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounter;
import com.finalproject.airport.airplane.gate.entity.Gate;
import com.finalproject.airport.common.BaseTimeEntity;
import com.finalproject.airport.facilities.entity.FacilitiesEntity;
import com.finalproject.airport.member.entity.UserEntity;
import com.finalproject.airport.storage.entity.StorageEntity;
import com.finalproject.airport.store.entity.StoreEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_managers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder(toBuilder = true)
public class ManagersEntity extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int managersCode;

    @Column(nullable = true)
    private Integer checkinCounterCode;

    @Column(nullable = true)
    private Integer facilitiesCode;

    @Column(nullable = true)
    private Integer storageCode;

    @Column(nullable = true)
    private Integer storeId;

    @Column(nullable = true)
    private Integer inspectionCode;

    @Column(nullable = true)
    private Integer baggageClaimCode;

    @Column(nullable = true)
    private Integer gateCode;

    @ManyToOne
    @JoinColumn(name = "user_code")
    private UserEntity user;

    @Column(name = "ISACTIVE", length = 1, nullable = false)
    @Setter
    private String isActive;

    @PrePersist
    private void ensureIsActiveDefault() {
        if (this.isActive == null) {
            this.isActive = "Y";
        }
    }


}


