package com.finalproject.airport.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StoreAPIDTO {

    @Schema(description = "입/출국 구역 (A: 입국장, D: 출국장)", example = "A")
    private String arrordep;

    @Schema(description = "매장명", example = "CU")
    private String entrpskoreannm;

    @Schema(description = "위치", example = "Terminal 1")
    private String lckoreannm;

    @Schema(description = "영업시간", example = "09:00 - 21:00")
    private String servicetime;

    @Schema(description = "전화번호", example = "010-9876-5432")
    private String tel;

    @Schema(description = "취급 품목", example = "편의점 관련 식품")
    private String trtmntprdlstkoreannm;
}
