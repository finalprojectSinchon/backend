package com.finalproject.airport.member.join;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinDTO {

    public int userCode;

    public String userId;

    public String userPassword;

    public String userEmail;

    public String userPhone;

    public String userAddress;

    private String userRole;


}
