package com.finalproject.airport.member.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserPasswordCheckDTO {

    private int userCode;
    private String userPassword;

}