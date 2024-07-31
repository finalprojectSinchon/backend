package com.finalproject.airport.storage.dto;

import com.finalproject.airport.storage.entity.Department;
import com.finalproject.airport.storage.entity.StorageType;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StorageDTO {

    // 창고명
    private int storageCode;

    // 창고위치
    private String location;

    // 창고상태
    private String status;

    // 창고타입
    private StorageType type;

    // 대분류
    private String category;

    // 담당부서
    private Department department;

    // 담당자
    private String manager;

    // 사용기간
    private String period;

    // 최근점검날짜
    private String date;

    // 활성 여부
    private String isActive;

}
