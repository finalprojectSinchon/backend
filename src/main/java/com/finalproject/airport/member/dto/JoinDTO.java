package com.finalproject.airport.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
public class JoinDTO {

    private int userCode;

    @NotBlank(message = "아이디는 필수 입력값 입니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수 입력값 입니다.")
    @Length(min = 8, message = "비밀번호는 8자 이상입력해야 합니다.")
    private String userPassword;

    @Email(message = "유효한 이메일 주소를 입력해 주세요.")
    @NotBlank(message = "이메일은 필수 입력값 입니다.")
    private String userEmail;

    @Pattern(regexp = "^\\d{10,11}$", message = "유효한 전화번호 형식을 입력해 주세요. (10-11자리 숫자)")
    @NotBlank(message = "휴대폰 번호는 필수 입력값 입니다.")
    private String userPhone;

    private String userAddress;

    @NotBlank(message = "이름은 필수 입력값 입니다.")
    private String userName;

    private String userAbout;


    private String userRole;
}
