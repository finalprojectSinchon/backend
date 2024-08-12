package com.finalproject.airport.storage.dto;

import com.finalproject.airport.member.dto.UserDTO;
import com.finalproject.airport.storage.entity.Department;
import com.finalproject.airport.storage.entity.StorageType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StorageDTO {

    // 창고명
    @Schema(description = "창고명")
    private int storageCode;

    // 창고위치
    @Schema(description = "창고위치")
    private String location;

    // 창고상태
    @Schema(description = "창고상태")
    private String status;

    // 창고타입
    @Schema(description = "창고타입")
    private StorageType type;

    // 대분류
    @Schema(description = "대분류")
    private String category;


    // 담당자
    @Schema(description = "담당자")
    private String manager;

    // 사용기간
    @Schema(description = "사용기간")
    private String period;

    // 최근점검날짜
    @Schema(description = "최근점검날짜")
    private String date;

    // 활성 여부
    @Schema(description = "활성 여부")
    private String isActive;

    @Schema(description = "승인요청자")
    private UserDTO approvalRequester;

}
