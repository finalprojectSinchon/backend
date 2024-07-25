package com.finalproject.airport.member.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserFindPasswordDTO {

    private String userEmail;

    private String userId;

    private String  userPhone;
}
