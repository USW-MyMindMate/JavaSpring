package com.example.MyMindMate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // test용
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable()) // POST 요청 문제 없음
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 모든 경로 인증 없이 허용
                )
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin())); // H2 콘솔 iframe 허용
        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:8081",     // iOS 시뮬레이터
                "http://10.0.2.2:8081",      // Android 에뮬레이터
                "http://127.0.0.1:8081"      // 로컬호스트 대체
        ));
        //configuration.setAllowedOrigins(List.of("")); // 개발용, 앱 어디서든 허용
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // 세션 쿠키 포함 가능

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


// // 🏁세션 사용 configuration
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // 프론트 주소
//        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(List.of("*"));
//        configuration.setAllowCredentials(true); // ★ 세션 쿠키 포함 허용
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())// 앱 전용이라 끄기
//                .cors(withDefaults())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/h2/**").permitAll()
//                        .requestMatchers("/user/**").permitAll()
//                        .requestMatchers("/api/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
//                .httpBasic(withDefaults()); // POST 요청에서도 Security가 최소 인증 체크만 함
//
////                .headers(headers -> headers
////                        .frameOptions(frame -> frame.sameOrigin()).httpBasic(withDefaults())
////                );
//
//        return http.build();
//    }
}



