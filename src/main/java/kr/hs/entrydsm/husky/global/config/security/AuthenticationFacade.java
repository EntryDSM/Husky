package kr.hs.entrydsm.husky.global.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Integer getReceiptCode() {
        Authentication auth = this.getAuthentication();
        if (auth.getPrincipal() instanceof AuthDetails) {
            return ((AuthDetails) auth.getPrincipal()).getUser().getReceiptCode();
        } else {
            return Integer.parseInt(this.getAuthentication().getName());
        }
    }

}
