package com.finalproject.airport.storage.entity;

import com.finalproject.airport.common.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "tbl_storage")
public class StorageEntity extends BaseTimeEntity {

    @Id
    private int storageCode;

    @Enumerated(EnumType.STRING)
    private StorageType storageType;

    private String storageCategory;


}
