package com.finalproject.airport.member.controller;

import com.finalproject.airport.auth.util.SMSUtil;
import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.member.dto.*;
import com.finalproject.airport.member.service.CustomUserDetails;
import com.finalproject.airport.member.service.JoinService;
import com.finalproject.airport.member.service.MailService;
import com.finalproject.airport.store.dto.AuthMailPhoneDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "회원 관련 컨트롤러", description = "회원 관리 및 인증 관련 API")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JoinService joinService;
    private final MailService mailService;
    private final SMSUtil smsUtil;

    @Operation(summary = "회원 가입", description = "회원가입을 처리합니다.")
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid JoinDTO joinDTO) {
        ResponseEntity<?> response = joinService.joinProcess(joinDTO);
        return response;
    }

    @Operation(summary = "회원 정보 수정", description = "사용자 정보를 수정합니다.")
    @PostMapping("/user")
    public ResponseEntity<?> modifyUser(@RequestBody @Valid UserModifyDTO userModifyDTO) {
        ResponseEntity<?> response = joinService.modifyUser(userModifyDTO);
        return response;
    }

    @Operation(summary = "비밀번호 확인", description = "사용자의 비밀번호가 맞는지 확인합니다.")
    @PostMapping("/api/v1/account/password-check")
    public ResponseEntity<?> passwordCheck(@RequestBody UserPasswordCheckDTO passwordCheckDTO) {
        Boolean correctPassword = joinService.passwordCheck(passwordCheckDTO);
        if (correctPassword) {
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "비밀번호가 일치합니다.", null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseDTO(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.", null));
        }
    }

    @Operation(summary = "비밀번호 변경", description = "사용자의 비밀번호를 변경합니다.")
    @PutMapping("/api/v1/account/change-password")
    public ResponseEntity<?> passwordChange(@RequestBody ChangePasswordDTO changePasswordDTO) {
        ResponseEntity<?> response = joinService.passwordChange(changePasswordDTO);
        return response;
    }

    @Operation(summary = "사용자 정보 조회", description = "현재 사용자의 정보를 조회합니다.")
    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO userDTO = userDetails.getUserDTO();
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공", userDTO));
    }

    @Operation(summary = "프로필 이미지 등록", description = "사용자의 프로필 이미지를 저장합니다.")
    @PostMapping("/api/v1/profile/img")
    public ResponseEntity<?> profileImg(@RequestBody Map<String, Object> info) {
        joinService.saveprofileImg(info);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "등록 성공", null));
    }

    @Operation(summary = "신규회원 인증코드 발급", description = "관리자가 신규회원 인증코드를 발급합니다.")
    @GetMapping("/api/v1/admin/code")
    public ResponseEntity<?> getAdminCode() {
        ResponseEntity<?> response = joinService.getAdminCode();
        return response;
    }

    @Operation(summary = "인증 코드 확인", description = "인증 코드를 확인합니다.")
    @PostMapping("/api/v1/auth")
    public ResponseEntity<?> checkAuthCode(@RequestBody Map<String, Integer> authCode) {
        return joinService.isCheckAuth(authCode);
    }

    @Operation(summary = "메일, 휴대폰 인증 코드 발송", description = "사용자에게 인증 코드를 메일, 휴대폰 으로 발송합니다.")
    @PostMapping("/api/v1/admin/auth/mail")
    public ResponseEntity<?> sendMail(@RequestBody AuthMailPhoneDTO authMailDTO) {
        try {
            if (authMailDTO.getUserEmail() != null && !authMailDTO.getUserEmail().isEmpty()) {
                mailService.sendForAuthCode(authMailDTO);
                if (authMailDTO.getUserPhone() != null && !authMailDTO.getUserPhone().isEmpty()) {
                    smsUtil.sendOne(authMailDTO.getUserPhone(), authMailDTO.getAuthCode());
                    return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "정상적으로 메일과 SMS를 보냈습니다.", null));
                }
                return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "정상적으로 메일을 보냈습니다.", null));
            } else if (authMailDTO.getUserPhone() != null && !authMailDTO.getUserPhone().isEmpty()) {
                smsUtil.sendOne(authMailDTO.getUserPhone(), authMailDTO.getAuthCode());
                return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "정상적으로 SMS를 보냈습니다.", null));
            } else {
                return ResponseEntity.badRequest().body(new ResponseDTO(HttpStatus.BAD_REQUEST, "이메일 또는 전화번호가 필요합니다.", null));
            }
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "메일 전송 오류: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류: " + e.getMessage(), null));
        }
    }

    @Operation(summary = "사용자 소개 변경", description = "사용자의 소개를 변경합니다.")
    @PostMapping("/api/v1/user-about")
    public ResponseEntity<?> userAboutChange(@RequestBody ChangeAboutDTO changeAboutDTO) {
        return joinService.userAboutChange(changeAboutDTO);
    }

    @PostMapping("/account/search-id")
    public ResponseEntity<?> id(@RequestBody UserFindIdDTO userFindIdDTO){


        return joinService.findUserId(userIdDTO);

        return joinService.findUserId(userFindIdDTO);
    }

    @PostMapping("/account/newPassword")
    public ResponseEntity<?> password (@RequestBody UserFindPasswordDTO userFindPasswordDTO){


        try {
            return joinService.findPwd(userFindPasswordDTO);
        } catch (MessagingException e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"서버 오류입니다.",null));
        }
    }
}
