package com.example.MyMindMate.config;

//import com.example.MyMindMate.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {
//🏁login session
//    @Override
//    public void addInterceptors(InterceptorRegistry registry){
//        registry.addInterceptor(new LoginCheckInterceptor())
//                .addPathPatterns(("/**"))
//                .excludePathPatterns(
//                        "/user/confirm-email",
//                        "/user/verify",
//                        "/user/check-account",
//                        "/user/sign-up-finish",
//                        "/user/login",
//                        "/user/reconfirm-email",
//                        "/error",
//                        "/css/**",
//                        "/js/**"
//                );
//    }

    // 👉 여기서 CORS 설정 추가
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 요청 허용
                .allowedOrigins("http://localhost:3000") // 프론트엔드 도메인
                .allowedMethods("*") // GET, POST 등 모든 메서드 허용
                .allowCredentials(true); // 세션 쿠키 등 자격정보 포함 허용
    }

}

