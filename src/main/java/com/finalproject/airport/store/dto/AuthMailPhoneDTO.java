package com.finalproject.airport.store.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AuthMailPhoneDTO {

    private int authCode;

    private String userEmail;

    private String userPhone;
}
