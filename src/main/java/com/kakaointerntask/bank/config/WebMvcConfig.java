package com.kakaointerntask.bank.config;

import com.kakaointerntask.bank.config.interceptor.SessionLoginInterceptor;
import com.kakaointerntask.bank.service.BankUserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AllArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    BankUserService bankUserService;

    @Bean
    public HandlerInterceptor sessionLoginInterceptor() {
        return new SessionLoginInterceptor(bankUserService);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionLoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/register", "/resources/**", "/api/**");
    }
}