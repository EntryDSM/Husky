package kr.hs.entrydsm.husky.domains.request;

import lombok.Data;

@Data
public class AccountRequest {

    private String email;
    private String password;

}
