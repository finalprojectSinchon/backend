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
    private String storageLocation;

    // 창고상태
    private String storageStatus;

    // 창고타입
    private StorageType storageType;

    // 대분류
    private String category;

    // 담당부서
    private Department department;

    // 담당자
    private String manager;
}
