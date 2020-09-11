package kr.hs.entrydsm.husky.global.config.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

public class ExceptionConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Override
    public void configure(HttpSecurity http) {
        ExceptionHandlerFilter handlerFilter = new ExceptionHandlerFilter();
        http.addFilterBefore(handlerFilter, JwtTokenFilter.class);
    }

}
