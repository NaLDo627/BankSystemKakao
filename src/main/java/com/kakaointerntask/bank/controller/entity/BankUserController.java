package com.kakaointerntask.bank.controller.entity;

import com.kakaointerntask.bank.annotation.BankUserAccessCheck;
import com.kakaointerntask.bank.common.CommonResponse;
import com.kakaointerntask.bank.annotation.ValidationCheck;
import com.kakaointerntask.bank.dto.BankUserDTO;
import com.kakaointerntask.bank.entity.BankUser;
import com.kakaointerntask.bank.exception.GlobalErrorCode;
import com.kakaointerntask.bank.exception.controller.RequestBadRequestException;
import com.kakaointerntask.bank.exception.controller.RequestForbiddenException;
import com.kakaointerntask.bank.service.AuthorizationService;
import com.kakaointerntask.bank.service.BankUserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class BankUserController {
    private final BankUserService bankUserService;
    private final AuthorizationService authorizationService;

    /* restful */
    @ValidationCheck
    @RequestMapping(value = "", consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
    ResponseEntity<CommonResponse<Void>> saveBankUser(@RequestBody @Valid BankUserDTO bankUserDTO, Errors errors) {
        /* 만약 회원 정보가 존재한다면, 200 응답을 보내지만 duplicate 메세지를 보낸다. */
        if (bankUserService.isIdBeingUsed(bankUserDTO.getId()))
            return ResponseEntity.ok(CommonResponse.getResponse("duplicate", 0));

        bankUserService.saveBankUserWithEncryptPassword(bankUserDTO.toNewEntity());
        return ResponseEntity.ok(CommonResponse.getOkResponse());
    }

    @BankUserAccessCheck(userNo = "#bankUserDTO.getUserNo()")
    @RequestMapping(value = "", consumes = "application/json", produces = "application/json", method = RequestMethod.PUT)
    ResponseEntity<CommonResponse<Void>> modifyBankUser(@RequestBody BankUserDTO bankUserDTO, HttpSession session) {
        if (bankUserDTO.getUserNo() == null)
            throw new RequestBadRequestException(GlobalErrorCode.BANKUSER_NOT_FOUND);

        bankUserService.modifyBankUser(bankUserDTO.toEntity());

        /* 세션에 유저 정보를 업데이트 한다. */
        session.setAttribute("bankUser", bankUserService.findBankUserByUserNo(bankUserDTO.getUserNo()));

        return ResponseEntity.ok(CommonResponse.getOkResponse());
    }

    /* Admin */
    @BankUserAccessCheck(userNo = "#userNo")
    @RequestMapping(value = "/change-rank", produces = "application/json", method = RequestMethod.PUT)
    ResponseEntity<CommonResponse<Void>> modifyBankUserRank(@RequestParam(value = "userNo") Integer userNo,
                                                            @RequestParam(value = "rank") String rank, HttpSession session) {

        authorizationService.checkAdminInSession(session);
        bankUserService.modifyBankUserRank(userNo, rank);
        return ResponseEntity.ok(CommonResponse.getOkResponse());
    }

    @BankUserAccessCheck(userNo = "#userNo")
    @RequestMapping(value = "", produces = "application/json", method = RequestMethod.DELETE)
    ResponseEntity<CommonResponse<Void>> removeBankUser(@RequestParam(value = "userNo") Integer userNo, HttpSession session) {
        authorizationService.checkAdminInSession(session);
        bankUserService.deleteBankUser(userNo);
        return ResponseEntity.ok(CommonResponse.getOkResponse());
    }
}
