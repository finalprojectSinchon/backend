package com.finalproject.airport.storage.entity;

import com.finalproject.airport.common.BaseTimeEntity;
import jakarta.persistence.*;
import jdk.jfr.Category;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_storage")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder(toBuilder= true)


public class StorageEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int storageCode;

    // 창고 위치
    private String storageLocation;

    // 창고 상태
    private String storageStatus;

    // 창고 타입
    @Enumerated(EnumType.STRING)
    private StorageType storageType;

    // 대분류
    private String category;

    // 담당부서
    @Enumerated(EnumType.STRING)
    private Department department;

    // 담당자
    private String manager;

    // 사용기간
    private String period;

    // 최근점검날짜
    private String date;


    @Column(name = "ISACTIVE", length = 1, nullable = false)
    private String isActive;

    @PrePersist
    private void ensureIsActiveDefault() {
        if (this.isActive == null) {
            this.isActive = "Y";
        }
    }
}
