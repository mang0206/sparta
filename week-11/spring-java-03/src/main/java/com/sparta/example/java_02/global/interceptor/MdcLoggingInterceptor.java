package com.sparta.example.java_02.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class MdcLoggingInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {
    // 요청이 들어올 때 MDC에 클라이언트 IP를 추가합니다.
    MDC.put("client_ip", request.getRemoteAddr());
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) {
    // 요청 처리가 완료되면 MDC에서 클라이언트 IP를 제거합니다.
    MDC.remove("client_ip");
  }
}
