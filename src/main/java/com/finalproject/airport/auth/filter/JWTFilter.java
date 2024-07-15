package com.finalproject.airport.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.airport.auth.util.JWTUtil;
import com.finalproject.airport.member.dto.UserDTO;
import com.finalproject.airport.member.entity.UserEntity;
import com.finalproject.airport.member.repository.UserRepository;
import com.finalproject.airport.member.service.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    public JWTFilter(JWTUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //request 에서 Authorization 헤더 검색
        String authorizationHeader = request.getHeader("Authorization");

        //Authorization 헤더 검증
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.info("token null");
            filterChain.doFilter(request, response);

            return;
        }

        log.info("JWT Token 취득 ");

        // token Bearer 부분 제거
        String token = authorizationHeader.split(" ")[1];

        // 토큰 소멸시간 검증
        if (jwtUtil.isExpired(token)) {

            System.out.println("token expired");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        String userId = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) {
            log.error("userId 찾을수 없음: " + userId);

            filterChain.doFilter(request, response);
            return;
        }

        // UserEntity에 사용자 정보 설정
        userEntity.setUserRole(role); // role은 토큰에서 가져온 값으로 설정

        // CustomUserDetails 객체 생성
        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        // Authentication 객체 생성 및 SecurityContextHolder에 설정
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 다음 필터로 이동
        filterChain.doFilter(request, response);
    }
}
