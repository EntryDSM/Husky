package kr.hs.entrydsm.husky.domain.user.service;

import kr.hs.entrydsm.husky.domain.process.service.ProcessServiceImpl;
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

    private final AuthenticationFacade authFacade;

    private final UserRepository userRepository;
    private final StatusRepository statusRepository;

    private final ProcessServiceImpl processService;

    public UserStatusResponse getStatus() {
        Integer receiptCode = authFacade.getReceiptCode();
        User user = userRepository.findById(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        Status status = statusRepository.findById(receiptCode)
                .orElseGet(() -> statusRepository.save(new Status(receiptCode)));

        return UserStatusResponse.response(user, status);
    }

    public UserStatusResponse finalSubmit() {
        Integer receiptCode = authFacade.getReceiptCode();
        User user = userRepository.findById(receiptCode)
                .orElseThrow(UserNotFoundException::new);

        Status status = statusRepository.findById(receiptCode)
                .orElseGet(() -> statusRepository.save(new Status(receiptCode)));

        if (!processService.AllCheck(user))
            throw new NotCompletedProcessException();

        status.finalSubmit();
        statusRepository.save(status);

        return UserStatusResponse.response(user, status);
    }

}
