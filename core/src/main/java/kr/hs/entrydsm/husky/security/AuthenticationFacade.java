package kr.hs.entrydsm.husky.security;

import kr.hs.entrydsm.husky.entities.users.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Integer getReceiptCode() {
        return ((User) this.getAuthentication().getPrincipal()).getReceiptCode();
    }

}