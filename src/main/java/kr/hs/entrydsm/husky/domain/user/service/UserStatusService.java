package kr.hs.entrydsm.husky.domain.user.service;

import kr.hs.entrydsm.husky.domain.process.service.ProcessService;
import kr.hs.entrydsm.husky.domain.user.dto.UserStatusResponse;
import kr.hs.entrydsm.husky.domain.user.exception.NotCompletedProcessException;
import kr.hs.entrydsm.husky.domain.user.exception.UserNotFoundException;
import kr.hs.entrydsm.husky.domain.user.domain.Status;
import kr.hs.entrydsm.husky.domain.user.domain.User;
import kr.hs.entrydsm.husky.domain.user.domain.repositories.StatusRepository;
import kr.hs.entrydsm.husky.domain.user.domain.repositories.UserRepository;
import kr.hs.entrydsm.husky.global.config.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserStatusService {

    private final UserRepository userRepository;
    private final StatusRepository statusRepository;

    private final AuthenticationFacade authenticationFacade;

    private final ProcessService processService;

    public UserStatusResponse getStatus() {
        Integer receiptCode = authenticationFacade.getReceiptCode();
        User user = userRepository.findById(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        Status status = user.getStatus();
        if (status == null) status = createStatus(user);

        return UserStatusResponse.response(user, status);
    }

    public UserStatusResponse finalSubmit() {
        Integer receiptCode = authenticationFacade.getReceiptCode();
        User user = userRepository.findById(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        Status status = user.getStatus();
        if (status == null) status = createStatus(user);

        if (!processService.AllCheck(user)) throw new NotCompletedProcessException();
        status.finalSubmit();
        statusRepository.save(status);

        return UserStatusResponse.response(user, status);
    }

    private Status createStatus(User user) {
        Status status = Status.builder()
                .receiptCode(user.getReceiptCode())
                .isFinalSubmit(false)
                .isPaid(false)
                .isPrintedApplicationArrived(false)
                .isPassedFirstApply(false)
                .isPassedInterview(false)
                .build();

        return statusRepository.save(status);
    }

}
