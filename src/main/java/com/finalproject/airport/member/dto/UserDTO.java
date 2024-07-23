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

    private String userAbout;

    private String userImg;

    private String isActive;

    public UserDTO(int userCode, String userId, String userEmail, String userPhone, String userAddress, String userName, String userRole, String userAbout, String userImg, String isActive) {
        this.userCode = userCode;
        this.userId = userId;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.userName = userName;
        this.userRole = userRole;
        this.userAbout = userAbout;
        this.userImg = userImg;
        this.isActive = isActive;
    }
}
