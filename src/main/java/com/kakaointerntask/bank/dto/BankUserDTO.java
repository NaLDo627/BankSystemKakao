package com.kakaointerntask.bank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kakaointerntask.bank.common.TimestampUtil;
import com.kakaointerntask.bank.entity.BankUser;
import com.kakaointerntask.bank.entity.enums.BankUserRank;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BankUserDTO {
    private Integer userNo;

    @NotBlank(message = "Id must not be blank")
    @Size(max = 32, message = "Id must not exceed size over 32")
    @Pattern(regexp = "^[a-zA-Z0-9.]*$", message = "Id must be either engilsh, number or dot(.)")
    private String id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(max = 64, message = "Password must not exceed size over 64")
    @NotBlank(message = "Password must not be blank")
    private String password;

    @NotBlank(message = "Name must not be blank")
    @Size(max = 32, message = "Name must not exceed size over 32")
    private String name;

    @Email(message = "Email format is not correct")
    @Size(max = 32, message = "Email must not exceed size over 32")
    private String email;

    @Size(max = 32, message = "Phone must not exceed size over 32")
    private String phone;

    public BankUser toNewEntity() {
        return new BankUser(null, this.id, this.password, this.name, this.email, this.phone,
                BankUserRank.NORMAL, false, true,
                TimestampUtil.now(), TimestampUtil.now(), null, null, null);
    }

    public BankUser toEntity() {
        return new BankUser(this.userNo, this.id, this.password, this.name, this.email, this.phone,
                null, false, true,
                null, null, null, null, null);
    }
}
