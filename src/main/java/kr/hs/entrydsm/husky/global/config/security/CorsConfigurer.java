package kr.hs.entrydsm.husky.global.config.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

public class CorsConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Override
    public void configure(HttpSecurity http) {
        CorsFilter corsFilter = new CorsFilter();
        http.addFilterBefore(corsFilter, RequestLogFilter.class);
    }

}
