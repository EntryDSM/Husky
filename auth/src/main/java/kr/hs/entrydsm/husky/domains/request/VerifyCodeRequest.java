package kr.hs.entrydsm.husky.domains.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class VerifyCodeRequest {

    @Email
    private String email;

    @NotBlank
    private String authCode;

}
