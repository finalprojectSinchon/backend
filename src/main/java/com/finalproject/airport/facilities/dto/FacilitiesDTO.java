package com.finalproject.airport.facilities.dto;

import com.finalproject.airport.facilities.entity.FacilitesType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class FacilitiesDTO {

    @Schema(description = "편의 시설 고유 코드", example = "123")
    private int facilitiesCode;

    @Schema(description = "편의 시설 상태", example = "활성")
    private String status;

    @Schema(description = "편의 시설 위치", example = "1번 게이트 옆")
    private String location;

    @Schema(description = "편의 시설 이름", example = "휴게실")
    private String facilitiesName;

    @Schema(description = "편의 시설 타입", example = "휴게실")
    private FacilitesType type;

    @Schema(description = "편의 시설 관리자", example = "김철수")
    private String manager;

    @Schema(description = "편의 시설 분류", example = "VIP")
    private String facilitiesClass;

    @Schema(description = "편의 시설 활성 상태", example = "Y")
    private String isActive;

    @Schema(description = "편의 시설 생성 일시", example = "2024-08-10T15:30:00")
    private LocalDateTime createdDate;

    @Schema(description = "편의 시설 비고", example = "추가 정보 필요")
    private String note;

    // 기본 생성자와 모든 필드를 포함한 생성자
    public FacilitiesDTO(int facilitiesCode, String status, String location, String facilitiesName, FacilitesType type, String manager, String facilitiesClass, String isActive, LocalDateTime createdDate, String note) {
        this.facilitiesCode = facilitiesCode;
        this.status = status;
        this.location = location;
        this.facilitiesName = facilitiesName;
        this.type = type;
        this.manager = manager;
        this.facilitiesClass = facilitiesClass;
        this.isActive = isActive;
        this.createdDate = createdDate;
        this.note = note;
    }

}
