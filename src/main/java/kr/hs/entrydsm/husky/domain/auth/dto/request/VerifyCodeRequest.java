package kr.hs.entrydsm.husky.domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyCodeRequest {

    @Email
    private String email;

    @NotBlank
    private String authCode;

}
