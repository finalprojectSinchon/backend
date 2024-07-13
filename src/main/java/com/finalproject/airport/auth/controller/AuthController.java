package com.finalproject.airport.auth.controller;

import com.finalproject.airport.member.join.JoinDTO;
import com.finalproject.airport.member.service.JoinService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Iterator;

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
    public ResponseEntity<?> join(JoinDTO joinDTO){

        System.out.println(joinDTO.getUserId());
        joinService.joinProcess(joinDTO);

        return ResponseEntity.ok().build();
    }

}
