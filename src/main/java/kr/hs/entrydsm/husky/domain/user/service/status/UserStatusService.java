package kr.hs.entrydsm.husky.domain.user.service.status;

import kr.hs.entrydsm.husky.domain.user.dto.UserPassResponse;
import kr.hs.entrydsm.husky.domain.user.dto.UserStatusResponse;

public interface UserStatusService {

    UserStatusResponse getStatus();
    UserStatusResponse finalSubmit();
    UserPassResponse isPassedFirst();
    UserPassResponse isPassedFinal();

}
