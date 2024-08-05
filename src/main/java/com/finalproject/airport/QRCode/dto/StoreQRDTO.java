package com.finalproject.airport.QRCode.dto;

import com.finalproject.airport.store.entity.StoreType;
import com.finalproject.airport.store.entity.StoreWork;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StoreQRDTO {

    @Schema(description = "점포 ID", example = "1")
    private int id;

    @Schema(description = "점포명", example = "CU")
    private String name;

    @Schema(description = "위치", example = "1터미널 2층 면세점")
    private String location;
}
