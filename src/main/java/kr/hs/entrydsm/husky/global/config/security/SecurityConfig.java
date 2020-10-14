package kr.hs.entrydsm.husky.global.config.security;

import kr.hs.entrydsm.husky.global.slack.SlackSenderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final SlackSenderManager slackSenderManager;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin().disable()
                .headers()
                    .frameOptions()
                    .disable().and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                    .antMatchers("/auth").permitAll()
                    .antMatchers("/users").permitAll()
                    .antMatchers("/users/password").permitAll()
                    .antMatchers("/users/email/verify").permitAll()
                    .antMatchers("/users/email/password/verify").permitAll()
                    .antMatchers("/actuator/health").permitAll()
                    .antMatchers("/schedules").permitAll()
                    .anyRequest().authenticated().and()
                .apply(new JwtConfigurer(jwtTokenProvider)).and()
                .apply(new ExceptionConfigurer(slackSenderManager)).and()
                .apply(new RequestLogConfigurer()).and()
                .apply(new CorsConfigurer());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
