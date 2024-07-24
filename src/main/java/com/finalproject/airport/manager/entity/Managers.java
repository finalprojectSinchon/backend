package com.finalproject.airport.manager.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "tbl_managers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder(toBuilder = true)
public class Managers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int managersCode;

    private int checkinCounterCode;
    private int airplaneCode;
    private int facilitiesCode;
    private int storageCode;
    private int storeId;
    private int inspectionCode;
    private int baggageClaimCode;
    private int gateCode;

    private int userCode;

    @Column(name = "ISACTIVE", length = 1, nullable = false)
    private String isActive;
    @PrePersist
    private void ensureIsActiveDefault() {
        if (this.isActive == null) {
            this.isActive = "Y";
        }
    }
}
