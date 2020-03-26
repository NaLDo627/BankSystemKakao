package com.kakaointerntask.bank.config.interceptor;

import com.kakaointerntask.bank.entity.BankUser;
import com.kakaointerntask.bank.service.BankUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@AllArgsConstructor
public class SessionLoginInterceptor extends HandlerInterceptorAdapter {
    private final BankUserService bankUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod))
            return super.preHandle(request, response, handler);

        if (request.getUserPrincipal() == null) {
            response.sendRedirect("/login");
            return false;
        }

        HttpSession httpSession = request.getSession();
        Object objBankUser = httpSession.getAttribute("bankUser");
        String authId = request.getUserPrincipal().getName();
        if (!(objBankUser instanceof BankUser) || !((BankUser) objBankUser).getId().equals(authId)) {
            BankUser bankUser = bankUserService.findBankUserById(authId);
            httpSession.setAttribute("bankUser", bankUser);
            log.info(String.format("Session info was set for id : \"%s\"", authId));
        }

        return super.preHandle(request, response, handler);
    }
}
