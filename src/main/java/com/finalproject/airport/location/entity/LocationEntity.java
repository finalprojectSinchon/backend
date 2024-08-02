package com.finalproject.airport.location.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_location")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder(toBuilder = true)
public class LocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer locationCode;

    @ManyToOne
    @JoinColumn(name = "zone_code")
    private ZoneEntity zone; // 구역 정보 (예: "A구역", "B구역")

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
