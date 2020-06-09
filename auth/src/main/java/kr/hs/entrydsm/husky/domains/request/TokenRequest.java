package kr.hs.entrydsm.husky.domains.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class TokenRequest {

    @NotBlank
    private String refreshToken;

}
