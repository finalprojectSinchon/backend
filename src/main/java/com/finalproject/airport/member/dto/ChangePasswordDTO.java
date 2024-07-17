package com.finalproject.airport.member.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChangePasswordDTO {

    private int userCode;

    private String newPassword;

    private String confirmPassword;
}
