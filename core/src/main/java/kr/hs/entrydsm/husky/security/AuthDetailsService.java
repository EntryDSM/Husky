package kr.hs.entrydsm.husky.security;

import kr.hs.entrydsm.husky.entities.users.repositories.UserRepository;
import kr.hs.entrydsm.husky.error.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public AuthDetails loadUserByUsername(String receiptCode) throws UsernameNotFoundException {
        return userRepository.findByReceiptCode(Integer.parseInt(receiptCode))
                .map(AuthDetails::new)
                .orElseThrow(UserNotFoundException::new);
    }

}
