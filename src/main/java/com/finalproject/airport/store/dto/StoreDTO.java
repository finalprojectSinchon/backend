package com.finalproject.airport.store.dto;

import com.finalproject.airport.store.entity.StoreType;
import com.finalproject.airport.store.entity.StoreWork;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StoreDTO {

    @Schema(description = "점포 ID", example = "1")
    private int storeId;

    @Schema(description = "점포명", example = "CU")
    private String storeName;

    @Schema(description = "주요업무", example = "식품 등 판매")
    private StoreWork storeWork;

    @Schema(description = "연락처", example = "010-9876-5432")
    private String storeContact;

    @Schema(description = "운영시간", example = "09:00 - 21:00")
    private String storeOperatingTime;

    @Schema(description = "취급 품목", example = "식품, 음료")
    private String storeItems;

    @Schema(description = "상태", example = "운영중")
    private String status;

    @Schema(description = "타입", example = "점포")
    private StoreType type;

    @Schema(description = "담당자", example = "홍길동")
    private String manager;

    @Schema(description = "활성 여부", example = "Y")
    private String isActive;

    @Schema(description = "비고", example = "특별 행사 중")
    private String storeExtra;

    @Schema(description = "위치", example = "1터미널 2층 면세점")
    private String storeLocation;
}
