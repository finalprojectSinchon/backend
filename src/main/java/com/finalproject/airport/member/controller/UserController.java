package com.finalproject.airport.member.controller;

import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.member.dto.UserContactDTO;
import com.finalproject.airport.member.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "유저에 관련돤 컨트롤러")
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/admin/contact")
    public ResponseEntity<?> contact() {

        List<UserContactDTO> contactDTO = userService.contact();

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"전체 유저 조회 성공", contactDTO));
    }

}
