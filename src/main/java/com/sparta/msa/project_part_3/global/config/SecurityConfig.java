package com.sparta.msa.project_part_3.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.sparta.msa.project_part_3.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  public static final String[] SECURITY_EXCLUDE_PATHS = {
      "/public/**", "/api/swagger-ui/**", "/swagger-ui/**", "/swagger-ui.html",
      "/api/v3/api-docs/**", "/v3/api-docs/**", "/favicon.ico", "/actuator/**",
      "/swagger-resources/**", "/external/**",
      "/api/users/login", "/api/users/registration"
  };

  private final ObjectMapper objectMapper;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        // CSRF 보호 비활성화 (REST API는 Stateless하므로 보통 비활성화)
        .csrf(AbstractHttpConfigurer::disable)
        // SecurityContext 저장소 설정 (HttpSession을 사용하도록 설정)
        .securityContext(context -> context
            .securityContextRepository(securityContextRepository())
        )
        // 세션 관리 설정
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 필요 시 세션 생성
            .maximumSessions(1) // 동시 접속 가능 세션 수 제한
            .maxSessionsPreventsLogin(false) // 기존 세션 만료 (false) vs 신규 로그인 차단 (true)
        )
        // 요청 권한 설정
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(SECURITY_EXCLUDE_PATHS).permitAll() // 인증 제외 경로
            .requestMatchers("/api/**").hasRole("USER") // /api/** 경로는 USER 권한 필요
            .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
        )
        // 폼 로그인 및 HTTP Basic 인증 비활성화 (JSON 기반 인증 사용 예정)
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        // 예외 처리 설정
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint((request, response, authException) -> {
              response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
              response.setContentType("application/json;charset=UTF-8");
              ApiResponse<Void> errorResponse = ApiResponse.<Void>builder()
                  .error(ApiResponse.Error.of("UNAUTHORIZED", "Authentication required"))
                  .build();
              response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            })
        );

    return http.build();
  }

  @Bean
  public SecurityContextRepository securityContextRepository() {
    return new HttpSessionSecurityContextRepository();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
