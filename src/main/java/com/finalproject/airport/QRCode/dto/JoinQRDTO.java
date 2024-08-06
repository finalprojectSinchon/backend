package com.finalproject.airport.QRCode.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class JoinQRDTO {

    private String manager;
    private String status;
    private String location;
    private String text;
    private String type;
    private String regularInspectionDate;
    private String phone;
    private int userCode;
    private int airplaneCode;

}
