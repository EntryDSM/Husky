package kr.hs.entrydsm.husky.domain.user.service;

import kr.hs.entrydsm.husky.domain.user.dto.UserStatusResponse;
import kr.hs.entrydsm.husky.domain.user.exception.UserNotFoundException;
import kr.hs.entrydsm.husky.entities.users.Status;
import kr.hs.entrydsm.husky.entities.users.User;
import kr.hs.entrydsm.husky.entities.users.repositories.StatusRepository;
import kr.hs.entrydsm.husky.entities.users.repositories.UserRepository;
import kr.hs.entrydsm.husky.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserStatusService {

    private final UserRepository userRepository;
    private final StatusRepository statusRepository;

    private final AuthenticationFacade authenticationFacade;

    public UserStatusResponse getStatus() {
        Integer receiptCode = authenticationFacade.getReceiptCode();
        User user = userRepository.findByReceiptCode(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        Status status = user.getStatus();
        if (status == null) status = createStatus(user);

        return UserStatusResponse.response(user, status);
    }

    public UserStatusResponse finalSubmit() {
        Integer receiptCode = authenticationFacade.getReceiptCode();
        User user = userRepository.findByReceiptCode(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        Status status = user.getStatus();
        if (status == null) status = createStatus(user);

        status.finalSubmit();
        statusRepository.save(status);

        return UserStatusResponse.response(user, status);
    }

    private Status createStatus(User user) {
        Status status = Status.builder()
                .receiptCode(user.getReceiptCode())
                .user(user)
                .isFinalSubmit(false)
                .isPaid(false)
                .isPrintedApplicationArrived(false)
                .isPassedFirstApply(false)
                .isPassedInterview(false)
                .build();

        return statusRepository.save(status);
    }

}
