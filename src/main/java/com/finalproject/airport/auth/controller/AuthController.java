package com.finalproject.airport.auth.controller;

import com.finalproject.airport.member.join.JoinDTO;
import com.finalproject.airport.member.service.JoinService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final JoinService joinService;

    public AuthController(JoinService joinService) {
        this.joinService = joinService;
    }

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/")
    public String home() {
        return "Hello user";
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(JoinDTO joinDTO){

        System.out.println(joinDTO.getUserId());
        joinService.joinProcess(joinDTO);

        return ResponseEntity.ok().build();
    }

}
