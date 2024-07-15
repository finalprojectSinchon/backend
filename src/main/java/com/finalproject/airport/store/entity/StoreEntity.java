package com.finalproject.airport.store.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "tbl_store")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder(toBuilder = true)
public class StoreEntity {

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
    private String storeStatus;

    // 타입
    @Enumerated(EnumType.STRING)
    private StoreType storeType;

    // 담당자
    private String storeManager;

}
