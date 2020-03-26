package com.kakaointerntask.bank.controller.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kakaointerntask.bank.common.ExceptionUtil;
import com.kakaointerntask.bank.common.JsonUtil;
import com.kakaointerntask.bank.entity.BankUser;
import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.exception.controller.RequestForbiddenException;
import com.kakaointerntask.bank.service.AuthorizationService;
import com.kakaointerntask.bank.service.BankUserService;
import com.kakaointerntask.bank.service.BankbookService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/")
public class BankUserViewController {
    BankUserService bankUserService;
    BankbookService bankbookService;
    AuthorizationService authorizationService;

    /* User page */
    @RequestMapping(value = "user-search")
    String getUserSearch(HttpServletRequest request, Authentication authentication, Model model){
        BankUser bankUser = (BankUser)request.getSession().getAttribute("bankUser");
        List<BankUser> bankUsers = bankUserService.
                findAllBankUsersExceptUserId(bankUser.getId());
        try {
            model.addAttribute("bankUsers", JsonUtil.toJson(bankUsers));
        } catch (JsonProcessingException e) {
            log.error(ExceptionUtil.getStackTrace(e));
            model.addAttribute("bankUsers", new ArrayList<>());
        }

        return "user/user-search";
    }

    @RequestMapping(value = "user-detail/{userNo}")
    String getUserDetail(@PathVariable("userNo") Integer userNo,
                         HttpServletRequest request,
                         Authentication authentication,
                         Model model) {
        BankUser bankUser = bankUserService.findBankUserByUserNo(userNo);
        if (bankUser.isAdmin())
            throw new RequestForbiddenException(GlobalErrorCode.BANKUSER_FOUND_BUT_ADMIN);

        model.addAttribute("bankUser", bankUser);
        model.addAttribute("bankbooks",
                bankbookService.findBankbooksByBankUserNo(bankUser.getUserNo()));
        return "user/user-detail";
    }

    /* admin page */
    @RequestMapping(value = "admin/user-detail/{userNo}")
    String getAdminUserDetail(@PathVariable("userNo") Integer userNo,
                              HttpServletRequest request,
                              Authentication authentication,
                              Model model) {
        BankUser bankUser = bankUserService.findBankUserByUserNo(userNo);
        if (bankUser.isAdmin())
            throw new RequestForbiddenException(GlobalErrorCode.BANKUSER_FOUND_BUT_ADMIN);

        model.addAttribute("bankUser", bankUser);
        model.addAttribute("bankbooks",
                bankbookService.findBankbooksByBankUserNo(bankUser.getUserNo()));
        return "admin/user-detail";
    }
}
