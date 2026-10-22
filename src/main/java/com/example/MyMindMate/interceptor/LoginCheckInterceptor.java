//package com.example.MyMindMate.interceptor;
//
//import com.example.MyMindMate.member.dto.UserDto;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import java.io.IOException;
//
//@Slf4j
//public class LoginCheckInterceptor implements HandlerInterceptor {
//
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        // 세션 가져오기 (새로 생성하지 않음)
//        HttpSession session = request.getSession(false);
//
//        response.setContentType("application/json; charset=UTF-8");
//
//        if (session == null) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
//            response.getWriter().write("{\"error\": \"세션이 없습니다.\"}");
//            log.info("세션이 없습니다.");
//            return false;
//        }
//
//        if (session.getAttribute("loginUser") == null) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
//            response.getWriter().write("{\"error\": \"로그인이 필요합니다.\"}");
//            log.info("로그인이 필요합니다.");
//            return false;
//        }
//        return HandlerInterceptor.super.preHandle(request, response, handler);
//    }
//
//}
