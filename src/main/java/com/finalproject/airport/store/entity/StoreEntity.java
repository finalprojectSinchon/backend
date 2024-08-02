package com.finalproject.airport.store.entity;

import com.finalproject.airport.common.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;


@Entity(name = "store")
@Table(name = "tbl_store")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder(toBuilder = true)
public class StoreEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int storeId;

    // 점포명
    private String storeName;

    // 주요업무
    @Enumerated(EnumType.STRING)
    private StoreWork storeWork;

     // 연락처
    private String storeContact;

    // 운영시간
    private String storeOperatingTime;

    // 취급품목
    private String storeItems;

    // 상태
    private String status;

    // 위치
    private String storeLocation;

    // 타입
    @Enumerated(EnumType.STRING)
    private StoreType type;

    // 담당자
    private String manager;

    @Column(name = "ISACTIVE", length = 1, nullable = false)
    private String isActive;

    @Column(columnDefinition = "TEXT")
    private String storeExtra;

    @PrePersist
    private void ensureIsActiveDefault() {
        if (this.isActive == null) {
            this.isActive = "Y";
        }
    }

    public StoreEntity(String storeName, StoreWork storeWork, String storeContact, String storeOperatingTime, String storeItems, String status, StoreType type, String manager, String storeLocation) {
        this.storeName = storeName;
        this.storeWork = storeWork;
        this.storeContact = storeContact;
        this.storeOperatingTime = storeOperatingTime;
        this.storeItems = storeItems;
        this.status = status;
        this.type = type;
        this.manager = manager;
        this.storeLocation = storeLocation;
    }
}
