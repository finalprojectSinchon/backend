package com.finalproject.airport.auth.config;

import com.finalproject.airport.auth.filter.JWTFilter;
import com.finalproject.airport.auth.filter.LoginFilter;
import com.finalproject.airport.auth.util.JWTUtil;
import com.finalproject.airport.member.repository.UserRepository;
import com.finalproject.airport.webSocket.handler.WebSocketHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebSocketConfigurer {


    private final AuthenticationConfiguration authenticationConfiguration;

    private final JWTUtil jwtUtil;

    private final WebSocketHandler webSocketHandler;

    //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil, WebSocketHandler webSocketHandler) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.webSocketHandler = webSocketHandler;
    }


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/ws")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .setAllowedOrigins("*"); // 또는 특정 도메인으로 제한
    }

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserRepository userRepository) throws Exception {

        // CORS 설정
        http.cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration configuration = new CorsConfiguration();

                // 특정 도메인 허용 설정 (http://localhost:5713)
                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:5713"));
                configuration.setAllowedMethods(Collections.singletonList("*"));  // 모든 HTTP 메서드 허용
                configuration.setAllowCredentials(true);  // 인증 정보 허용
                configuration.setAllowedHeaders(Collections.singletonList("*"));  // 모든 헤더 허용
                configuration.setMaxAge(3600L);  // Preflight 요청 결과를 캐시할 시간 설정

                configuration.setExposedHeaders(Collections.singletonList("Authorization"));  // 노출할 응답 헤더 설정

                return configuration;
            }
        })));

        //csrf disable
        http
                .csrf((auth) -> auth.disable());

        //From 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //http basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());


        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login", "/", "/join","/api/v1/auth","/ws/**", "/account/**").permitAll()
                        .requestMatchers("/api/v1/admin/qr/","/api/v1/admin/auth/mail").hasRole("ADMIN")
                        .requestMatchers("/api/v1/store/**").hasAnyRole("STORE","ADMIN")
                        .requestMatchers("/api/v1/airplane/**").hasAnyRole("AIRPLANE","ADMIN")
                        .requestMatchers("/user-info","/api/v1/location/**","/user").authenticated()      // 로그인 한 사용자만 접근 가능
                        .requestMatchers("/api/v1/inspection/**","/api/v1/managers").authenticated()
                        .requestMatchers("/**").authenticated()
                        .anyRequest().authenticated());

        http
                .addFilterBefore(new JWTFilter(jwtUtil, userRepository), LoginFilter.class);

        // CORS 필터 추가

        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration),jwtUtil), UsernamePasswordAuthenticationFilter.class);

        //세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
