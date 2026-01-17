package com.sparta.example.java_02.global.config;

import com.sparta.example.java_02.global.interceptor.MdcLoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

  private final MdcLoggingInterceptor mdcLoggingInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // 모든 요청에 대해 MdcLoggingInterceptor가 동작하도록 추가합니다.
    registry.addInterceptor(mdcLoggingInterceptor);
  }
}
