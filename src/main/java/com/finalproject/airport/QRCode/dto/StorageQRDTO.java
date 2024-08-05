package com.finalproject.airport.QRCode.dto;

import com.finalproject.airport.storage.entity.StorageType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StorageQRDTO {

    private int id;

    private String location;

    private StorageType type;
}
