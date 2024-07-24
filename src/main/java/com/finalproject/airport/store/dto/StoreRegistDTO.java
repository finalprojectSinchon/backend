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
public class StoreRegistDTO {

    @Schema(description = "점포명", example = "CU")
    private String storeName;

    @Schema(description = "주요업무", example = "판매")
    private StoreWork storeWork;

    @Schema(description = "연락처", example = "010-9876-5432")
    private String storeContact;

    @Schema(description = "운영시간", example = "09:00 - 21:00")
    private String storeOperatingTime;

    @Schema(description = "취급 품목", example = "식품 음료 등")
    private String storeItems;

    @Schema(description = "상태", example = "운영중")
    private String storeStatus;

    @Schema(description = "타입", example = "점포")
    private StoreType storeType;

    @Schema(description = "담당자", example = "홍길동")
    private String storeManager;
}
