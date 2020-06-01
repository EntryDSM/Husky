package kr.hs.entrydsm.husky.domains.request;

import jdk.jfr.DataAmount;

@DataAmount
public class AuthCodeRequest {

    private String email;
    private String authCode;

}
