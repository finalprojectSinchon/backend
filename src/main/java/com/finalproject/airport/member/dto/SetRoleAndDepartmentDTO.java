package com.finalproject.airport.member.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SetRoleAndDepartmentDTO {

    private int userCode;

    private String role;

    private String department;
}
