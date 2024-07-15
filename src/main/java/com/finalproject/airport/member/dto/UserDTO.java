package com.finalproject.airport.member.dto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserDTO {

    public int userCode;

    public String userId;

    public String userPassword;

    public String userEmail;

    public String userPhone;

    public String userAddress;

    public String userName;

    private String userRole;

    public UserDTO(int userCode, String userId, String userEmail, String userPhone, String userAddress, String userName, String userRole) {
        this.userCode = userCode;
        this.userId = userId;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.userName = userName;
        this.userRole = userRole;
    }
}
