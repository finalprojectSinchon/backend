package com.finalproject.airport.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewUserDetailDTO {

    private int userCode;

    private String userName;

    private String userId;

    private String userEmail;

    private String userPhone;

    private String userRole;

}
