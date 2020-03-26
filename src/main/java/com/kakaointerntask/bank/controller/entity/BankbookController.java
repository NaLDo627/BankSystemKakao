package com.kakaointerntask.bank.controller.entity;

import com.kakaointerntask.bank.common.CommonResponse;
import com.kakaointerntask.bank.annotation.BankbookAccessCheck;
import com.kakaointerntask.bank.annotation.ValidationCheck;
import com.kakaointerntask.bank.dto.BankbookDTO;
import com.kakaointerntask.bank.entity.BankUser;
import com.kakaointerntask.bank.service.AuthorizationService;
import com.kakaointerntask.bank.service.BankbookService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bankbook")
public class BankbookController {
    private final BankbookService bankbookService;

    /* restful */
    @ValidationCheck
    @RequestMapping(value = "", consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
    ResponseEntity<CommonResponse<Void>> saveBankbook(@RequestBody @Valid BankbookDTO bankbookDTO,
                                                  Errors errors) {
        bankbookService.saveBankbookFromDTO(bankbookDTO);
        return ResponseEntity.ok(CommonResponse.getOkResponse());
    }

    @BankbookAccessCheck(bookNo = "#bookNo")
    @RequestMapping(value = "", produces = "application/json", method = RequestMethod.DELETE)
    ResponseEntity<CommonResponse<Void>> removeBankbook(@RequestParam(value = "bookNo") Integer bookNo, HttpServletRequest request) {
        BankUser bankUser = (BankUser) request.getSession().getAttribute("bankUser");
        bankbookService.deleteBankbook(bookNo, bankUser.isAdmin());
        return ResponseEntity.ok(CommonResponse.getOkResponse());
    }

    @BankbookAccessCheck(bookNo = "#bookNo")
    @RequestMapping(value = "/change-type", produces = "application/json", method = RequestMethod.PUT)
    ResponseEntity<CommonResponse<Void>> modifyBankbookType(@RequestParam(value = "bookNo") Integer bookNo,
                                                            @RequestParam(value = "type") String type, HttpServletRequest request) {

        bankbookService.modifyBankbookType(bookNo, type);
        return ResponseEntity.ok(CommonResponse.getOkResponse());
    }
}
