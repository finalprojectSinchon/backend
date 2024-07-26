package com.finalproject.airport.member.entity;

import com.finalproject.airport.common.BaseTimeEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "tbl_user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder(toBuilder = true)
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_code")
    public int userCode;

    @Column(name = "user_id")
    @Comment(value = "유저 아이디")
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

    @Column(name = "user_about", columnDefinition = "TEXT")
    public String userAbout;

    @Setter
    @Column(name = "user_role")
    private String userRole;

    @Column(name = "ISACTIVE", length = 1, nullable = false)
    private String isActive;

    @Column(columnDefinition = "TEXT")
    private String userImg;

    @Column(name = "user_department")
    private String userDepartment;

    @Setter
    private int authCode;



    @PrePersist
    private void ensureIsActiveDefault() {
        if (this.isActive == null) {
            this.isActive = "Y";
        }
        if ( this.userImg == null) {
            this.userImg = "https://i.namu.wiki/i/KTpTMexyA3WHmFNhXwOdo3nT_GURxYuA8tEedFzGqFRWXRg78FtmecK21BIsn7qUddH-Ch9z7k64BA7dVTdSaI-0QK5oftkiOgvEc5RuJmYzrJECd3Oa3GNOJUptym3vMN9P3PCYMEycdftWYayZYg.webp";
        }
    }


    public UserEntity(String userEmail, String userPhone, String userAddress, String userName, String userAbout) {
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.userName = userName;
        this.userAbout = userAbout;
    }

    public UserEntity(String userId, String userPassword, String userEmail, String userPhone, String userAddress, String userRole, String userName, String userAbout, String isActive) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.userRole = userRole;
        this.userName = userName;
        this.userAbout = userAbout;
        this.isActive = isActive;
    }
}
