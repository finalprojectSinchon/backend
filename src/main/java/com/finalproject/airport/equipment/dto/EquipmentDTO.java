package com.finalproject.airport.equipment.dto;

import com.finalproject.airport.equipment.entity.EquipmentCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EquipmentDTO {

    @Schema(description = "장비 코드", example = "12345")
    private int equipmentCode;

    @Schema(description = "장비 이름", example = "지상 조종 장비")
    private String equipmentName;

    @Schema(description = "장비 가격", example = "500000")
    private int equipmentPrice;

    @Schema(description = "장비 수량", example = "10")
    private int equipmentQuantity;

    @Schema(description = "장비 위치", example = "1번 창고")
    private String location;

    @Schema(description = "장비 카테고리", example = "GROUND_CONTROL")
    private EquipmentCategory category;

    @Schema(description = "장비 상태", example = "활성")
    private String status;

    @Schema(description = "위치 PK", example = "1")
    private int zoneCode;

    @Schema(description = "장비 이미지 URL", example = "http://example.com/image.jpg")
    private String img;
}
