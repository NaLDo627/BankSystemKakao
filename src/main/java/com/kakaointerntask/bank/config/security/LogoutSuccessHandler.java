package com.kakaointerntask.bank.config.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
                                        throws ServletException, IOException {
        log.info(String.format("id : \"%s\" logged out successfully", authentication.getName()));

        HttpSession session = request.getSession();
        session.removeAttribute("bankUser");
        log.info(String.format("Session info was removed for id : \"%s\"", authentication.getName()));

        super.onLogoutSuccess(request, response, authentication);
    }
}
