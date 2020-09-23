package kr.hs.entrydsm.husky.global.config.security;

import kr.hs.entrydsm.husky.global.slack.SlackSenderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

@RequiredArgsConstructor
public class ExceptionConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final SlackSenderManager slackSenderManager;

    @Override
    public void configure(HttpSecurity http) {
        ExceptionHandlerFilter handlerFilter = new ExceptionHandlerFilter(slackSenderManager);
        http.addFilterBefore(handlerFilter, JwtTokenFilter.class);
    }

}
