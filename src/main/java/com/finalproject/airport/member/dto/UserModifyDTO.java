package com.finalproject.airport.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserModifyDTO {

    private int userCode;

    @NotBlank(message = "이름은 필수 입력값 입니다.")
    private String userName;

    @Email(message = "유효한 이메일 주소를 입력해 주세요.")
    @NotBlank(message = "이메일은 필수 입력값 입니다.")
    private String userEmail;

    @Pattern(regexp = "^\\d{10,11}$", message = "유효한 전화번호 형식을 입력해 주세요. (10-11자리 숫자)")
    @NotBlank(message = "휴대폰 번호는 필수 입력값 입니다.")
    private String userPhone;


    private String userAddress;
    private String userAbout;


}
