package com.finalproject.airport.member.dto;

import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserContactDTO {

    // userDB
    private int userCode;
    // userDB
    private String userName;
    // userDB
    private String userEmail;
    // userDB
    private String userPhone;
    // userDB
    private String userAddress;
    // userDB
    private String userRole;
    // userDB
    private String userAbout;
    // userDB
    private String userImg;

    private Boolean deleted;

    private String createdDate;

}
