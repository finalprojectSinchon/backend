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

    private int storageCode;

    private String storageLocation;

    private String storageStatus;

    private StorageType storageType;

    private String category;

    private Department department;

    private String manager;
}
