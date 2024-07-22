package com.finalproject.airport.store.dto;

import com.finalproject.airport.store.entity.StoreType;
import com.finalproject.airport.store.entity.StoreWork;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StoreDTO {

    private int storeId;

    // 점포명
    // entrpskoreannm
    private String storeName;

    // 주요업무
    // trtmntprdlstkoreannm
    private StoreWork storeWork;

    // 연락처
    // tel
    private String storeContact;

    // 운영시간
    // servicetime
    private String storeOperatingTime;

    // 취급품목
    // trtmntprdlstkoreannm
    private String storeItems;

    // 상태
    // lckoreannm
    private String storeStatus;

    // 타입
    private StoreType storeType;

    // 담당자
    private String storeManager;

    // 활성여부
    private String isActive;

    // 비고
    private String storeExtra;

}
