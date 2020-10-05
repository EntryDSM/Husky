package kr.hs.entrydsm.husky.domain.process.service;

import kr.hs.entrydsm.husky.domain.application.domain.CalculatedScore;
import kr.hs.entrydsm.husky.domain.process.dto.ProcessResponse;
import kr.hs.entrydsm.husky.domain.user.domain.User;

public interface ProcessService {

    ProcessResponse getProcess();
    boolean allCheck(User user, CalculatedScore score);

}
