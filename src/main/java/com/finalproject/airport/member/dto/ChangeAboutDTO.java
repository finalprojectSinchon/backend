package com.finalproject.airport.member.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChangeAboutDTO {

    private int userCode;

    private String userAbout;
}
