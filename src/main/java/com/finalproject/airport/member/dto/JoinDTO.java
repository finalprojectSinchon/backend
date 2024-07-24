package com.finalproject.airport.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Tag(name = "회원가입 관련 DTO", description = "회원가입을 위한 데이터 전송 객체")
@Getter
@Setter
@ToString
public class JoinDTO {

    @Schema(description = "사용자 고유 코드", example = "1001")
    private int userCode;

    @Schema(description = "사용자 아이디", example = "user123")
    @NotBlank(message = "아이디는 필수 입력값 입니다.")
    private String userId;

    @Schema(description = "사용자 비밀번호", example = "password123")
    @NotBlank(message = "비밀번호는 필수 입력값 입니다.")
    @Length(min = 8, message = "비밀번호는 8자 이상입력해야 합니다.")
    private String userPassword;

    @Schema(description = "사용자 이메일 주소", example = "user@example.com")
    @Email(message = "유효한 이메일 주소를 입력해 주세요.")
    @NotBlank(message = "이메일은 필수 입력값 입니다.")
    private String userEmail;

    @Schema(description = "사용자 전화번호", example = "01012345678")
    @Pattern(regexp = "^\\d{10,11}$", message = "유효한 전화번호 형식을 입력해 주세요. (10-11자리 숫자)")
    @NotBlank(message = "휴대폰 번호는 필수 입력값 입니다.")
    private String userPhone;

    @Schema(description = "사용자 주소", example = "서울특별시 강남구")
    private String userAddress;

    @Schema(description = "사용자 이름", example = "홍길동")
    @NotBlank(message = "이름은 필수 입력값 입니다.")
    private String userName;

    @Schema(description = "사용자 소개", example = "안녕하세요, 홍길동입니다.")
    private String userAbout;

    @Schema(description = "인증 코드", example = "123456")
    private int authCode;

    @Schema(description = "사용자 역할", example = "USER")
    private String userRole;
}
