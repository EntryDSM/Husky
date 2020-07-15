package kr.hs.entrydsm.husky.domains.request;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
public class VerifyCodeRequest {

    @Email
    private String email;

    @NotBlank
    private String authCode;

}
