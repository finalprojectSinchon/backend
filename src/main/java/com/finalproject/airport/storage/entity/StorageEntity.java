package com.finalproject.airport.storage.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_storage")
public class StorageEntity {

    @Id
    private int storageCode;

    @Enumerated(EnumType.STRING)
    private StorageType storageType;

    private String storageCategory;


}
