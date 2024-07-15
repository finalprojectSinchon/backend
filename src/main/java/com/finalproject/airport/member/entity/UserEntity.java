package com.finalproject.airport.member.entity;

import com.finalproject.airport.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_code")
    public int userCode;

    @Column(name = "user_id")
    public String userId;

    @Column(name = "user_password")
    public String userPassword;

    @Column(name = "user_email")
    public String userEmail;

    @Column(name = "user_phone")
    public String userPhone;

    @Column(name = "user_address")
    public String userAddress;

    @Column(name = "user_name")
    public String userName;

    @Setter
    @Column(name = "user_role")
    private String userRole;



    public UserEntity(String userId, String userPassword, String userEmail, String userPhone, String userAddress, String userRole, String userName) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.userRole = userRole;
        this.userName = userName;
    }
}
