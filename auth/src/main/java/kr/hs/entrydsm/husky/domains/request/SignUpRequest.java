package kr.hs.entrydsm.husky.domains.request;

import lombok.Data;

@Data
public class SignUpRequest {

    private String email;
    private String password;

}
