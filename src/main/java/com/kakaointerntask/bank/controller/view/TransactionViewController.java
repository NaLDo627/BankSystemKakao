package com.kakaointerntask.bank.controller.view;

import com.kakaointerntask.bank.annotation.TransactionHistoryAccessCheck;
import com.kakaointerntask.bank.entity.BankUser;
import com.kakaointerntask.bank.exception.service.BankbookNotFoundException;
import com.kakaointerntask.bank.service.BankUserService;
import com.kakaointerntask.bank.service.BankbookService;
import com.kakaointerntask.bank.service.TransactionHistoryService;
import com.kakaointerntask.bank.service.implement.TransactionTransferService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/")
public class TransactionViewController {
    BankUserService bankUserService;
    BankbookService bankbookService;
    TransactionTransferService transactionTransferService;
    TransactionHistoryService transactionHistoryService;

    /* User page */
    @RequestMapping(value = "transaction")
    String getTransaction(@RequestParam(value = "bookNo", defaultValue = "0", required = false) Integer bookNo,
                          @RequestParam(value = "receiveBookId", defaultValue = "", required = false) String receiveBookId,
                          HttpServletRequest request,
                          Model model){
        BankUser bankUser = (BankUser)request.getSession().getAttribute("bankUser");

        /* 자신의 소유가 아닌 통장이 쿼리스트링으로 들어오면 프론트에서 처리 가능하도록 0으로 셋 */
        try {
            if (!bankbookService.isBankUserOwnedBankbook(bankUser.getUserNo(), bookNo))
                bookNo = 0;
        } catch (BankbookNotFoundException e) {
            bookNo = 0;
        }

        model.addAttribute("transactionBookNo", bookNo);
        model.addAttribute("receiveBookId", receiveBookId);
        model.addAttribute("bankUser", bankUser);
        model.addAttribute("bankbooks", bankbookService.findBankbooksByBankUserNo(bankUser.getUserNo()));
        model.addAttribute("isTransferChargeOccurs",
                transactionTransferService.isTransferChargeOccurs(bankUser.getUserNo()));
        return "user/transaction";
    }

    @TransactionHistoryAccessCheck(historyNo = "#historyNo")
    @RequestMapping(value = "transaction-detail/{historyNo}")
    String getTransactionDetail(@PathVariable("historyNo") Long historyNo,
                                HttpServletRequest request,
                                Model model){
        BankUser bankUser = (BankUser)request.getSession().getAttribute("bankUser");
        model.addAttribute("transactionHistory", transactionHistoryService.findTransactonHistoryByBankUserNo(historyNo, bankUser.getUserNo()));
        return "user/transaction-detail";
    }
}
