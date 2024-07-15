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
    private String storeName;

    // 주요업무
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
    private StoreType storeType;

    // 담당자
    private String storeManager;

}
