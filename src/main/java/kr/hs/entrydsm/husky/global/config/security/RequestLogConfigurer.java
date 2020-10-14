package kr.hs.entrydsm.husky.global.config.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

public class RequestLogConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Override
    public void configure(HttpSecurity http) {
        RequestLogFilter filter = new RequestLogFilter();
        http.addFilterBefore(filter, ExceptionHandlerFilter.class);
    }

}
