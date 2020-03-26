package com.kakaointerntask.bank.controller.view;

import com.kakaointerntask.bank.common.SecurityUtil;
import com.kakaointerntask.bank.entity.BankUser;
import com.kakaointerntask.bank.service.BankUserService;
import com.kakaointerntask.bank.service.BankbookService;
import com.kakaointerntask.bank.service.TransactionHistoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/")
public class MainViewController {
    BankUserService bankUserService;
    BankbookService bankbookService;
    TransactionHistoryService transactionHistoryService;

    /* User page */
    @RequestMapping(value = "login")
    String getLogin(HttpServletRequest request, Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()){
            if (SecurityUtil.hasRole(authentication.getAuthorities(), "ROLE_ADMIN"))
                return "redirect:/admin";
            return "redirect:/";
        }

        String referer = request.getHeader("Referer");
        request.getSession().setAttribute("prevPage", referer);
        return "login";
    }

    @RequestMapping(value = "register")
    String getRegister(Model model){
        return "register";
    }

    @RequestMapping(value = {"", "index"})
    String getIndex(HttpServletRequest request, Authentication authentication, Model model){
        BankUser bankUser = (BankUser)request.getSession().getAttribute("bankUser");
        model.addAttribute("bankUser", bankUser);
        model.addAttribute("bankbooks", bankbookService.findBankbooksByBankUserNo(bankUser.getUserNo()));
        model.addAttribute("transactionHistorys", transactionHistoryService.findTransactionHistorysByBankUserNo(bankUser.getUserNo()));
        return "user/index";
    }

    @RequestMapping(value = "profile")
    String getProfile(HttpServletRequest request, Authentication authentication, Model model){
        BankUser bankUser = (BankUser)request.getSession().getAttribute("bankUser");
        model.addAttribute("bankUser", bankUser);
        return "user/profile";
    }

    /* admin page */
    @RequestMapping(value = "admin")
    String getAdmin(HttpServletRequest request, Authentication authentication, Model model){
        model.addAttribute("bankbooks", bankbookService.findAllBankbooks());
        model.addAttribute("bankUsers", bankUserService.findAllBankUsersExceptAdmin());
        return "admin/index";
    }
}
