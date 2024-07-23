package com.finalproject.airport.store.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AuthMailDTO {

    private int authCode;

    private String userEmail;
}
