package com.kakaointerntask.bank.controller.view;

import com.kakaointerntask.bank.annotation.BankbookAccessCheck;
import com.kakaointerntask.bank.entity.BankUser;
import com.kakaointerntask.bank.entity.Bankbook;
import com.kakaointerntask.bank.service.BankCreditService;
import com.kakaointerntask.bank.service.BankUserService;
import com.kakaointerntask.bank.service.BankbookService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/")
public class BankbookViewController {
    BankUserService bankUserService;
    BankbookService bankbookService;
    BankCreditService bankCreditService;

    /* User page */
    @RequestMapping(value = "new-bankbook")
    String getNewBankbook(HttpServletRequest request, Model model){
        BankUser bankUser = (BankUser)request.getSession().getAttribute("bankUser");

        /* 만약 한도를 초과한 통장이 있다면, Model에 flag set */
        model.addAttribute("creditBankbookLimitReached",
                bankCreditService.isBankUserReachedCreditLimit(bankUser.getUserNo()));
        model.addAttribute("bankUser", bankUser);
        return "user/new-bankbook";
    }

    @BankbookAccessCheck(bookNo = "#bookNo")
    @RequestMapping(value = "bankbook-detail/{bookNo}")
    String getBankbookDetail(@PathVariable("bookNo") Integer bookNo,
                             HttpServletRequest request,
                             Model model){
        Bankbook bankbook = bankbookService.findBankbookByBookNo(bookNo);
        model.addAttribute("bankbook", bankbook);

        return "user/bankbook-detail";
    }

    /* admin page */
    @RequestMapping(value = "admin/bankbook-detail/{bookNo}")
    String getAdminBankbookDetail(@PathVariable("bookNo") Integer bookNo,
                                  HttpServletRequest request,
                                  Model model){
        Bankbook bankbook = bankbookService.findBankbookByBookNo(bookNo);
        model.addAttribute("bankbook", bankbook);
        return "admin/bankbook-detail";
    }
}
