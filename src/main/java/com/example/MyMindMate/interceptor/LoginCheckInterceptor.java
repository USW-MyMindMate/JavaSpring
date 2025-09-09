package com.example.MyMindMate.interceptor;

import com.example.MyMindMate.member.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 세션에서 회원 정보 조회
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("loginUser") == null){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write("{\"error\": \"로그인이 필요합니다.\"}");
//            log.info("세션이 없습니다.");
//            response.sendRedirect("/user/login");
            return false;
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

}
