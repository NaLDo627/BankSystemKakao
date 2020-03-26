package com.kakaointerntask.bank.config.security;

import com.kakaointerntask.bank.common.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    public LoginSuccessHandler(String defaultTargetUrl) {
        setDefaultTargetUrl(defaultTargetUrl);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
                                        throws ServletException, IOException {
        log.info(String.format("id : \"%s\" logged in successfully", authentication.getName()));

        HttpSession session = request.getSession();


        /* 만약 다른 페이지에서 로그인 페이지로 넘어왔다면 해당 페이지로 리다이렉트 */
        Optional<String> optRedirectUrl = Optional.ofNullable((String)session.getAttribute("prevPage"));
        String redirectUrl = optRedirectUrl.orElse("");
        if (redirectUrl.equals("/login")) {
            session.removeAttribute("prevPage");
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
            return;
        }

        if (SecurityUtil.hasRole(authentication.getAuthorities(), "ROLE_ADMIN")) {
            getRedirectStrategy().sendRedirect(request, response, "/admin");
            return;
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
