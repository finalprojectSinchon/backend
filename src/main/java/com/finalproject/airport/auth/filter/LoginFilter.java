package com.finalproject.airport.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.airport.auth.util.JWTUtil;
import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.member.dto.UserDTO;
import com.finalproject.airport.member.service.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 아이디, 비밀번호 로그인 세션에서 얻어옴
        String userId = request.getParameter("userId");
        String userPassword = request.getParameter("userPassword");

        System.out.println("userId = " + userId);
        System.out.println("userPassword = " + userPassword);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userId, userPassword, null);

        return authenticationManager.authenticate(authToken);
    }

    // 성공 시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        log.info("Successful authentication");

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String userId = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(userId, role, 60 * 60 * 24 * 1000L);

        response.addHeader("Authorization", "Bearer " + token);

        // body 설정
        response.setContentType("application/json; charset=utf-8");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> JSONToken = new HashMap<>();
        JSONToken.put("Authorization", "Bearer " + token);
        String jsonResponse = objectMapper.writeValueAsString(new ResponseDTO(HttpStatus.OK,"로그인 성공",JSONToken));
        response.getWriter().write(jsonResponse);
    }


    // 실패 시 실행되는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("login failed");
        response.setStatus(401);

        response.setContentType("application/json; charset=utf-8");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(new ResponseDTO(HttpStatus.UNAUTHORIZED,"로그인 실패",null));
        response.getWriter().write(jsonResponse);
    }
}
