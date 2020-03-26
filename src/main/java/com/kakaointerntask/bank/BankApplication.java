package com.kakaointerntask.bank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.util.StringUtils;

@Slf4j
@EnableAspectJAutoProxy
@SpringBootApplication
public class BankApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BankApplication.class);
    }

    public static void main(String[] args) {
        String profile = System.getProperty("spring.profiles.active");
        if(profile == null || "default".equals(profile)) {
            System.setProperty("spring.profiles.active", "dev");
            profile = "dev";
        }

        log.info("spring.profiles.active : " + profile);
        SpringApplication.run(BankApplication.class, args);
    }
}