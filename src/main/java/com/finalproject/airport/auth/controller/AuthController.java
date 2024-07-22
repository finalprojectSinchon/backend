package com.finalproject.airport.auth.controller;

import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.member.dto.*;
import com.finalproject.airport.member.service.CustomUserDetails;
import com.finalproject.airport.member.service.JoinService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

@RestController
public class AuthController {

    private final JoinService joinService;

    public AuthController(JoinService joinService) {
        this.joinService = joinService;
    }

    @GetMapping("/api/hello")
    public String hello() {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        return "Hello World" + name ;
    }

    @GetMapping("/")
    public String home() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        return "Hello user" + role;
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid JoinDTO joinDTO){

        ResponseEntity<?> response = joinService.joinProcess(joinDTO);

        return response;
    }

    @PostMapping("/user")
    public ResponseEntity<?> modifyUser(@RequestBody @Valid UserModifyDTO userModifyDTO){

        ResponseEntity<?> response = joinService.modifyUser(userModifyDTO);

        return response;
    }

    @PostMapping("/api/v1/account/password-check")
    public ResponseEntity<?> passwordCheck(@RequestBody UserPasswordCheckDTO passwordCheckDTO){

        Boolean CorrectPassword = joinService.passwordCheck(passwordCheckDTO);

        if(CorrectPassword){
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"비밀번호가 일치합니다.",null));
        } else {
            return ResponseEntity.badRequest().body(new ResponseDTO(HttpStatus.BAD_REQUEST,"비밀번호가 일치하지 않습니다.",null));
        }

    }

    @PutMapping("/api/v1/account/change-password")
    public ResponseEntity<?> passwordChange(@RequestBody ChangePasswordDTO changePasswordDTO){

        ResponseEntity<?> response = joinService.passwordChange(changePasswordDTO);

        return response;
    }


    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO userDTO =  userDetails.getUserDTO();

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"조회 성공",userDTO));
    }

    @PostMapping("/api/v1/profile/img")
    public ResponseEntity<?> profileImg(@RequestBody Map<String,Object> info){

        joinService.saveprofileImg(info);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"등록 성공",null));
    }

    @GetMapping("/api/v1/admin/code")
    public ResponseEntity<?> getAdminCode(){

        ResponseEntity<?> response = joinService.getAdminCode();

        return response;
    }

}
