package eodigatji.eodigatjiserver.post.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // API 테스트를 위해 CSRF 보호 비활성화
                .authorizeHttpRequests(auth -> auth
                        // Swagger 문서 관련 주소는 누구나 접근 가능하게 허용
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        // 방금 만든 게시판 API도 접근 허용
                        .requestMatchers("/v1/posts/**").permitAll()
                        // 그 외의 모든 요청도 일단 허용 (나중에 로그인 기능 붙일 때 제한하면 됩니다)
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}