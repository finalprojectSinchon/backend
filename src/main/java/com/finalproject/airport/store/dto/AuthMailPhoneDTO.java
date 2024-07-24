package com.finalproject.airport.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AuthMailPhoneDTO {

    @Schema(description = "인증 코드", example = "123456")
    private int authCode;

    @Schema(description = "사용자 이메일 주소", example = "example@test.com")
    private String userEmail;

    @Schema(description = "사용자 전화번호", example = "01012345678")
    private String userPhone;
}
